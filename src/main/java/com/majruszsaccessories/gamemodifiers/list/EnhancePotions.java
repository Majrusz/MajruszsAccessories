package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryInteger;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Utility;
import com.mlib.gamemodifiers.contexts.OnPotionBrewedContext;
import com.mlib.gamemodifiers.data.OnPotionBrewedData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.item.alchemy.PotionUtils.TAG_CUSTOM_POTION_EFFECTS;

public class EnhancePotions extends AccessoryModifier {
	final AccessoryPercent duration = new AccessoryPercent( "potion_duration_penalty", "Duration penalty for created enhanced potions.", false, 0.5, 0.0, 1.0, -1.0 );
	final AccessoryInteger amplifier = new AccessoryInteger( "potion_extra_amplifier", "Extra potion level for created enhanced potions.", false, 1, 1, 10 );

	public EnhancePotions( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnPotionBrewedContext onPotionBrewed = new OnPotionBrewedContext( this.toAccessoryConsumer( this::enhancePotion ) );
		onPotionBrewed.addCondition( data->PotionUtils.getMobEffects( data.itemStack ).size() > 0 ).addCondition( data->!data.itemStack.getOrCreateTag()
			.contains( TAG_CUSTOM_POTION_EFFECTS ) ).addConfigs( this.duration, this.amplifier );

		this.addContext( onPotionBrewed );
		this.addTooltip( this.amplifier, "majruszsaccessories.bonuses.potion_amplifier" );
		this.addTooltip( this.duration, "majruszsaccessories.bonuses.potion_duration" );
	}

	private void enhancePotion( OnPotionBrewedData data, AccessoryHandler handler ) {
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
