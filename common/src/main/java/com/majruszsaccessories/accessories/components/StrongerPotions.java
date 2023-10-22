package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnItemBrewed;
import com.mlib.data.Serializable;
import com.mlib.level.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

public class StrongerPotions extends BonusComponent< AccessoryItem > {
	RangedFloat durationPenalty = new RangedFloat().id( "duration_penalty" ).maxRange( Range.of( 0.0f, 1.0f ) );
	RangedInteger amplifier = new RangedInteger().id( "amplifier" ).maxRange( Range.of( 1, 20 ) );

	public static ISupplier< AccessoryItem > create( float durationPenalty, int amplifier ) {
		return handler->new StrongerPotions( handler, durationPenalty, amplifier );
	}

	protected StrongerPotions( BonusHandler< AccessoryItem > handler, float durationPenalty, int amplifier ) {
		super( handler );

		this.durationPenalty.set( durationPenalty, Range.of( 0.0f, 1.0f ) );
		this.amplifier.set( amplifier, Range.of( 1, 10 ) );

		OnItemBrewed.listen( this::boostPotions )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->LevelHelper.getNearestPlayer( data.level, data.blockPos, 10.0f ) ) )
			.addCondition( data->data.items.subList( 0, 3 ).stream().anyMatch( itemStack->!PotionUtils.getMobEffects( itemStack ).isEmpty() ) );

		this.addTooltip( "majruszsaccessories.bonuses.potion_amplifier", TooltipHelper.asValue( this.amplifier ) );
		this.addTooltip( "majruszsaccessories.bonuses.potion_duration", TooltipHelper.asPercent( this.durationPenalty ).bonusMultiplier( -1.0f ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "stronger_potion", subconfig->{
			this.durationPenalty.define( subconfig );
			this.amplifier.define( subconfig );
		} );
	}

	private void boostPotions( OnItemBrewed data ) {
		AccessoryHolder holder = CustomConditions.getLastHolder();
		data.mapPotions( potions->{
			float durationMultiplier = 1.0f - holder.apply( this.durationPenalty, -1.0f );
			int extraAmplifier = holder.apply( this.amplifier );

			return potions.stream()
				.map( itemStack->{
					List< MobEffectInstance > effects = PotionUtils.getMobEffects( itemStack );
					if( effects.isEmpty() ) {
						return itemStack;
					}

					ItemStack potion = new ItemStack( Items.POTION );
					new Data().write( potion.getOrCreateTag() );

					return PotionUtils.setCustomEffects( potion, PotionUtils.getMobEffects( itemStack )
						.stream()
						.map( effect->new MobEffectInstance( effect.getEffect(), Math.max( 40, ( int )( effect.getDuration() * durationMultiplier ) ), effect.getAmplifier() + extraAmplifier ) )
						.toList()
					);
				} )
				.toList();
		} );
		this.spawnEffects( data );
	}

	private void spawnEffects( OnItemBrewed data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 6 )
			.emit( data.getServerLevel(), AnyPos.from( data.blockPos ).center().vec3() );
	}

	private static class Data extends Serializable {
		private String name = "{\"translate\":\"majruszsaccessories.bonuses.potion_name\",\"italic\":false}";

		public Data() {
			this.defineCustom( "display", config->config.defineString( "Name", ()->this.name, x->this.name = x ) );
		}
	}
}
