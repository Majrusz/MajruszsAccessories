package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryInteger;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Utility;
import com.mlib.gamemodifiers.contexts.OnPotionBrewed;
import com.mlib.math.Range;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.item.alchemy.PotionUtils.TAG_CUSTOM_POTION_EFFECTS;

public class EnhancePotions extends AccessoryModifier {
	final AccessoryPercent duration;
	final AccessoryInteger amplifier;

	public EnhancePotions( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.6, 1 );
	}

	public EnhancePotions( Supplier< ? extends AccessoryItem > item, String configKey, double durationPenalty, int extraLevel ) {
		super( item, configKey );

		this.duration = new AccessoryPercent( durationPenalty, new Range<>( 0.0, 1.0 ), -1.0 );
		this.amplifier = new AccessoryInteger( extraLevel, new Range<>( 1, 10 ) );

		new OnPotionBrewed.Context( this.toAccessoryConsumer( this::enhancePotion ) )
			.addCondition( data->PotionUtils.getMobEffects( data.itemStack ).size() > 0 )
			.addCondition( data->!data.itemStack.getOrCreateTag().contains( TAG_CUSTOM_POTION_EFFECTS ) )
			.addConfig( this.duration.name( "potion_duration_penalty" ).comment( "Duration penalty for created enhanced potions." ) )
			.addConfig( this.amplifier.name( "potion_extra_amplifier" ).comment( "Extra potion level for created enhanced potions." ) )
			.insertTo( this );

		this.addTooltip( this.amplifier, "majruszsaccessories.bonuses.potion_amplifier" );
		this.addTooltip( this.duration, "majruszsaccessories.bonuses.potion_duration" );
	}

	private void enhancePotion( OnPotionBrewed.Data data, AccessoryHandler handler ) {
		List< MobEffectInstance > effects = PotionUtils.getMobEffects( data.itemStack );
		List< MobEffectInstance > enhancedEffects = this.getEnhancedEffects( handler, effects );
		PotionUtils.setCustomEffects( data.itemStack, enhancedEffects );
		this.updatePotionName( data.itemStack );
	}

	private void updatePotionName( ItemStack itemStack ) {
		itemStack.getOrCreateTag().putString( PotionUtils.TAG_POTION, Potions.AWKWARD.getName( "" ) );
		itemStack.getOrCreateTagElement( "display" ).putString( "Name", "{\"translate\":\"majruszsaccessories.bonuses.potion_name\",\"italic\":false}" );
	}

	private List< MobEffectInstance > getEnhancedEffects( AccessoryHandler handler, List< MobEffectInstance > effects ) {
		List< MobEffectInstance > enhancedEffects = new ArrayList<>();
		float durationMultiplier = 1.0f - this.duration.getValue( handler );
		int extraAmplifier = this.amplifier.getValue( handler );
		for( MobEffectInstance effect : effects ) {
			int duration = Math.max( Utility.secondsToTicks( 2.0 ), ( int )( effect.getDuration() * durationMultiplier ) );
			int amplifier = effect.getAmplifier() + extraAmplifier;
			enhancedEffects.add( new MobEffectInstance( effect.getEffect(), duration, amplifier ) );
		}

		return enhancedEffects;
	}
}
