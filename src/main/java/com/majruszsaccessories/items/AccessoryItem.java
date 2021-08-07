package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.MajruszsAccessories;
import com.mlib.MajruszLibrary;
import com.mlib.config.AvailabilityConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.majruszsaccessories.MajruszsAccessories.ACCESSORIES_GROUP;

/** Class with common code for all Accessory items. */
public class AccessoryItem extends Item {
	private static final List< AccessoryItem > ACCESSORY_ITEMS = new ArrayList<>();
	private static final String INVENTORY_TOOLTIP_TRANSLATION_KEY = "majruszs_accessories.items.accessory_item";
	private static final String EFFECTIVENESS_TRANSLATION_KEY = "majruszs_accessories.items.effectiveness";
	private static final String EFFECTIVENESS_TAG = "Effectiveness";
	private static final String EFFECTIVENESS_VALUE_TAG = "Value";
	protected final ConfigGroup group;
	protected final AvailabilityConfig effectiveness;
	protected final DoubleConfig minimumEffectiveness;
	protected final DoubleConfig maximumEffectiveness;
	private final String registryKey, tooltipTranslationKey, hintTranslationKey;

	public AccessoryItem( String configName, String registryKey, boolean withExtraHint ) {
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

		this.registryKey = registryKey;
		this.tooltipTranslationKey = "item.majruszs_accessories." + registryKey + ".item_tooltip";
		this.hintTranslationKey = withExtraHint ? ( "item.majruszs_accessories." + registryKey + ".hint" ) : null;

		ACCESSORY_ITEMS.add( this );
	}

	public AccessoryItem( String configName, String registryKey ) {
		this( configName, registryKey, false );
	}

	/** Adds tooltip with information what this accessory does and its effectiveness level. */
	@Override
	@OnlyIn( Dist.CLIENT )
	public void appendHoverText( ItemStack itemStack, @Nullable Level level, List< Component > tooltip, TooltipFlag flag ) {
		tooltip.add( new TranslatableComponent( this.tooltipTranslationKey ).withStyle( ChatFormatting.GOLD ) );
		if( isEffectivenessEnabled() && getEffectiveness( itemStack ) != 0.0 ) {
			MutableComponent text = new net.minecraft.network.chat.TextComponent( getEffectiveness( itemStack ) > 0.0 ? "+" : "" );
			text.append( "" + ( int )( getEffectiveness( itemStack ) * 100 ) + "% " );
			text.append( new TranslatableComponent( EFFECTIVENESS_TRANSLATION_KEY ) );
			text.withStyle( getEffectivenessColor( itemStack ) );

			tooltip.add( text );
		}

		if( this.hintTranslationKey != null )
			MajruszsAccessories.addAdvancedTooltip( tooltip, flag, this.hintTranslationKey );

		if( !Integration.isCuriosInstalled() )
			MajruszsAccessories.addAdvancedTooltips( tooltip, flag, " ", INVENTORY_TOOLTIP_TRANSLATION_KEY );
	}

	/** Adds 3 variants with different effectiveness bonuses to creative mode tab. */
	@Override
	public void fillItemCategory( CreativeModeTab itemGroup, NonNullList< ItemStack > itemStacks ) {
		if( !allowdedIn( itemGroup ) )
			return;

		double max = this.maximumEffectiveness.get(), min = this.minimumEffectiveness.get();
		double range = max - min;
		for( int i = 0; i < 3; ++i )
			itemStacks.add( setEffectiveness( new ItemStack( this ), Math.round( 100.0 * ( min + range * ( i + 1 ) * 0.25 ) ) / 100.0 ) );
	}

	/** Registers all accessory items in the game. */
	public static void registerAll( DeferredRegister< Item > deferredRegister ) {
		new Instances(); // this line only forces Instances to load first

		for( AccessoryItem item : ACCESSORY_ITEMS )
			deferredRegister.register( item.registryKey, ()->item );
	}

	/** Checks whether item stack has effectiveness tag. */
	public static boolean hasEffectivenessTag( ItemStack itemStack ) {
		return itemStack.getOrCreateTagElement( EFFECTIVENESS_TAG )
			.contains( EFFECTIVENESS_VALUE_TAG );
	}

	/** Returns current effectiveness from item stack. */
	public static double getEffectiveness( ItemStack itemStack ) {
		return !( itemStack.getItem() instanceof AccessoryItem ) ? 0.0 : itemStack.getOrCreateTagElement( EFFECTIVENESS_TAG )
			.getDouble( EFFECTIVENESS_VALUE_TAG );
	}

	/** Returns color depending on current effectiveness value. */
	@OnlyIn( Dist.CLIENT )
	public ChatFormatting getEffectivenessColor( ItemStack itemStack ) {
		double value = getEffectiveness( itemStack );
		if( value == this.maximumEffectiveness.get() )
			return ChatFormatting.GOLD;
		else if( value < 0.0 )
			return ChatFormatting.RED;
		else
			return ChatFormatting.GREEN;
	}

	/** Returns whether effectiveness mechanic is enabled and is valid. */
	public boolean isEffectivenessEnabled() {
		return this.effectiveness.isEnabled() && this.minimumEffectiveness.get() <= this.maximumEffectiveness.get();
	}

	/** Sets random effectiveness in given item stack. */
	public void setRandomEffectiveness( ItemStack itemStack ) {
		if( !isEffectivenessEnabled() )
			return;

		double gaussianRandom = Mth.clamp( MajruszLibrary.RANDOM.nextGaussian() / 3.0, -1.0,
			1.0
		); // random value from range [-1.0; 1.0] with mean ~= 0.0 and standard deviation ~= 0.3333..
		double gaussianRandomShifted = ( gaussianRandom + 1.0 ) / 2.0; // random value from range [0.0; 1.0] with mean ~= 0.5 and standard deviation ~= 0.1666..
		double randomValue = gaussianRandomShifted * ( this.maximumEffectiveness.get() - this.minimumEffectiveness.get() ) + this.minimumEffectiveness.get(); // random value from range set in config [-min; max]
		setEffectiveness( itemStack, Math.round( randomValue * 100.0 ) / 100.0 );
	}

	/** Sets effectiveness bonus to given item stack if possible. */
	public ItemStack setEffectiveness( ItemStack itemStack, double effectiveness ) {
		CompoundTag nbt = itemStack.getOrCreateTagElement( EFFECTIVENESS_TAG );
		nbt.putDouble( EFFECTIVENESS_VALUE_TAG, Mth.clamp( effectiveness, this.minimumEffectiveness.get(), this.maximumEffectiveness.get() ) );

		return itemStack;
	}

	/** Checks whether player have this item in inventory. */
	protected boolean hasAny( Player player ) {
		if( Integration.isCuriosInstalled() )
			return CuriosApi.getCuriosHelper()
				.findEquippedCurio( this, player )
				.isPresent();

		Set< Item > items = new HashSet<>();
		items.add( this );

		return player.getInventory()
			.hasAnyOf( items );
	}

	/** Returns highest effectiveness bonus in the inventory of this item. (with curios it only returns value from pocket slot) */
	protected double getHighestEffectiveness( Player player ) {
		if( Integration.isCuriosInstalled() )
			return CuriosApi.getCuriosHelper()
				.findEquippedCurio( this, player )
				.map( accessoryItemStruct->AccessoryItem.getEffectiveness( accessoryItemStruct.right ) )
				.orElse( 0.0 );

		double bonus = this.minimumEffectiveness.get();
		for( ItemStack itemStack : player.getInventory().items )
			if( this.equals( itemStack.getItem() ) && getEffectiveness( itemStack ) > bonus )
				bonus = getEffectiveness( itemStack );

		return bonus;
	}

	/** Returns item stack of Accessory Item with random effectiveness bonus. */
	protected ItemStack getRandomInstance() {
		ItemStack itemStack = new ItemStack( this );
		setRandomEffectiveness( itemStack );

		return itemStack;
	}
}
