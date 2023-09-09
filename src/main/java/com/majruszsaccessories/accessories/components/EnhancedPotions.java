package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.Utility;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.OnPotionBrewed;
import com.mlib.math.Range;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.item.alchemy.PotionUtils.TAG_CUSTOM_POTION_EFFECTS;

public class EnhancedPotions extends AccessoryComponent {
	final DoubleConfig duration;
	final IntegerConfig amplifier;

	public static ISupplier create( double durationPenalty, int extraLevel ) {
		return ( item, group )->new EnhancedPotions( item, group, durationPenalty, extraLevel );
	}

	public static ISupplier create() {
		return create( 0.6, 1 );
	}

	protected EnhancedPotions( Supplier< AccessoryItem > item, ConfigGroup group, double durationPenalty, int extraLevel ) {
		super( item );

		this.duration = new DoubleConfig( durationPenalty, new Range<>( 0.0, 0.99 ) );
		this.amplifier = new IntegerConfig( extraLevel, new Range<>( 1, 10 ) );

		OnPotionBrewed.listen( this::enhancePotion )
			.addCondition( Condition.predicate( data->PotionUtils.getMobEffects( data.itemStack ).size() > 0 ) )
			.addCondition( Condition.predicate( data->!data.itemStack.getOrCreateTag().contains( TAG_CUSTOM_POTION_EFFECTS ) ) )
			.addCondition( CustomConditions.hasAccessory( item, data->data.player ) )
			.addConfig( this.duration.name( "potion_duration_penalty" ).comment( "Duration penalty for created enhanced potions." ) )
			.addConfig( this.amplifier.name( "potion_extra_amplifier" ).comment( "Extra potion level for created enhanced potions." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.potion_amplifier", TooltipHelper.asValue( this.amplifier ) );
		this.addTooltip( "majruszsaccessories.bonuses.potion_duration", TooltipHelper.asPercent( this.duration ).bonusMultiplier( -1.0f ) );
	}

	private void enhancePotion( OnPotionBrewed.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.item.get() );
		List< MobEffectInstance > effects = PotionUtils.getMobEffects( data.itemStack );
		List< MobEffectInstance > enhancedEffects = this.getEnhancedEffects( holder, effects );
		PotionUtils.setCustomEffects( data.itemStack, enhancedEffects );
		this.updatePotionName( data.itemStack );
	}

	private void updatePotionName( ItemStack itemStack ) {
		itemStack.getOrCreateTag().putString( PotionUtils.TAG_POTION, Potions.AWKWARD.getName( "" ) );
		itemStack.getOrCreateTagElement( "display" ).putString( "Name", "{\"translate\":\"majruszsaccessories.bonuses.potion_name\",\"italic\":false}" );
	}

	private List< MobEffectInstance > getEnhancedEffects( AccessoryHolder holder, List< MobEffectInstance > effects ) {
		List< MobEffectInstance > enhancedEffects = new ArrayList<>();
		float durationMultiplier = 1.0f - holder.apply( this.duration, -1.0 );
		int extraAmplifier = holder.apply( this.amplifier );
		for( MobEffectInstance effect : effects ) {
			int duration = Math.max( Utility.secondsToTicks( 2.0 ), ( int )( effect.getDuration() * durationMultiplier ) );
			int amplifier = effect.getAmplifier() + extraAmplifier;
			enhancedEffects.add( new MobEffectInstance( effect.getEffect(), duration, amplifier ) );
		}

		return enhancedEffects;
	}
}
