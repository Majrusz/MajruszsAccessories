package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.MajruszsAccessories;
import com.mlib.MajruszLibrary;
import com.mlib.config.AvailabilityConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.majruszsaccessories.MajruszsAccessories.ACCESSORIES_GROUP;

/** Class with common code for all Accessory items. */
public class AccessoryItem extends Item {
	private static final String INVENTORY_TOOLTIP_TRANSLATION_KEY = "majruszs_accessories.items.inventory_item";
	private static final String EFFECTIVENESS_TRANSLATION_KEY = "majruszs_accessories.items.effectiveness";
	private static final String EFFECTIVENESS_TAG = "Effectiveness";
	private static final String EFFECTIVENESS_VALUE_TAG = "Value";
	protected final ConfigGroup group;
	protected final AvailabilityConfig effectiveness;
	protected final DoubleConfig minimumEffectiveness;
	protected final DoubleConfig maximumEffectiveness;
	private final String translationKey;

	public AccessoryItem( String configName, String translationKeyID ) {
		super( ( new Properties() ).stacksTo( 1 )
			.rarity( Rarity.RARE )
			.tab( Instances.ITEM_GROUP ) );

		String availabilityComment = "Is the effectiveness mechanic enabled?";
		this.effectiveness = new AvailabilityConfig( "effectiveness", availabilityComment, false, true );

		String minComment = "Minimum effectiveness bonus.";
		this.minimumEffectiveness = new DoubleConfig( "minimum_effectiveness", minComment, false, -0.6, -1.0, 10.0 );

		String maxComment = "Maximum effectiveness bonus.";
		this.maximumEffectiveness = new DoubleConfig( "maximum_effectiveness", maxComment, false, 0.6, -1.0, 10.0 );

		String groupComment = "Functionality of " + configName + ".";
		this.group = ACCESSORIES_GROUP.addGroup( new ConfigGroup( configName.replace( " ", "" ), groupComment ) );
		this.group.addConfigs( this.effectiveness, this.minimumEffectiveness, this.maximumEffectiveness );

		this.translationKey = "item.majruszs_accessories." + translationKeyID + ".item_tooltip";
	}

	/** Adds tooltip with information what this accessory does and its effectiveness level. */
	@Override
	@OnlyIn( Dist.CLIENT )
	public void appendHoverText( ItemStack itemStack, @Nullable World world, List< ITextComponent > tooltip, ITooltipFlag flag ) {
		tooltip.add( new TranslationTextComponent( this.translationKey ).withStyle( TextFormatting.GOLD ) );
		if( isEffectivenessEnabled() && getEffectiveness( itemStack ) != 0.0 ) {
			IFormattableTextComponent text = new StringTextComponent( getEffectiveness( itemStack ) > 0.0 ? "+" : "" );
			text.append( "" + ( int )( getEffectiveness( itemStack ) * 100 ) + "% " );
			text.append( new TranslationTextComponent( EFFECTIVENESS_TRANSLATION_KEY ) );
			text.withStyle( getEffectivenessColor( itemStack ) );

			tooltip.add( text );
		}

		if( !Integration.isCuriosInstalled() )
			MajruszsAccessories.addAdvancedTooltips( tooltip, flag, " ", INVENTORY_TOOLTIP_TRANSLATION_KEY );
	}

	/** Checks whether item stack has effectiveness tag. */
	public static boolean hasEffectivenessTag( ItemStack itemStack ) {
		return itemStack.getOrCreateTagElement( EFFECTIVENESS_TAG )
			.contains( EFFECTIVENESS_VALUE_TAG );
	}

	/** Returns current effectiveness from item stack. */
	public static double getEffectiveness( ItemStack itemStack ) {
		if( !( itemStack.getItem() instanceof AccessoryItem ) )
			return 0.0;

		return itemStack.getOrCreateTagElement( EFFECTIVENESS_TAG )
			.getDouble( EFFECTIVENESS_VALUE_TAG );
	}

	/** Returns color depending on current effectiveness value. */
	@OnlyIn( Dist.CLIENT )
	public TextFormatting getEffectivenessColor( ItemStack itemStack ) {
		double value = getEffectiveness( itemStack );
		if( value == this.maximumEffectiveness.get() )
			return TextFormatting.GOLD;
		else if( value < 0.0 )
			return TextFormatting.RED;
		else
			return TextFormatting.GREEN;
	}

	/** Returns whether effectiveness mechanic is enabled and is valid. */
	public boolean isEffectivenessEnabled() {
		return this.effectiveness.isEnabled() && this.minimumEffectiveness.get() <= this.maximumEffectiveness.get();
	}

	/** Sets random effectiveness in given item stack. */
	public void setRandomEffectiveness( ItemStack itemStack ) {
		if( !isEffectivenessEnabled() )
			return;

		double gaussianRandom = MathHelper.clamp( MajruszLibrary.RANDOM.nextGaussian() / 3.0, -1.0,
			1.0
		); // random value from range [-1.0; 1.0] with mean ~= 0.0 and standard deviation ~= 0.3333..
		double gaussianRandomShifted = ( gaussianRandom + 1.0 ) / 2.0; // random value from range [0.0; 1.0] with mean ~= 0.5 and standard deviation ~= 0.1666..
		double randomValue = gaussianRandomShifted * ( this.maximumEffectiveness.get() - this.minimumEffectiveness.get() ) + this.minimumEffectiveness.get(); // random value from range set in config [-min; max]
		CompoundNBT nbt = itemStack.getOrCreateTagElement( EFFECTIVENESS_TAG );
		nbt.putDouble( EFFECTIVENESS_VALUE_TAG, Math.round( randomValue * 100.0 ) / 100.0 );
	}

	/** Checks whether player have this item in inventory. */
	protected boolean hasAny( PlayerEntity player, AccessoryItem item ) {
		Set< Item > items = new HashSet<>();
		items.add( item );

		return player.inventory.hasAnyOf( items );
	}

	/** Returns highest inventory item effectiveness. */
	protected double getHighestEffectiveness( PlayerEntity player, AccessoryItem item ) {
		double bonus = this.minimumEffectiveness.get();
		for( ItemStack itemStack : player.inventory.items )
			if( item.equals( itemStack.getItem() ) && item.getEffectiveness( itemStack ) > bonus )
				bonus = item.getEffectiveness( itemStack );

		return bonus;
	}

	/** Spawns special particles at given position. */
	protected void spawnParticles( Vector3d position, ServerWorld world, double offset ) {
		world.sendParticles( ParticleTypes.HAPPY_VILLAGER, position.x, position.y, position.z, 5, offset, offset, offset, 0.1 );
	}
}
