package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.events.OnItemBrewed;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

public class StrongerPotions extends BonusComponent< AccessoryItem > {
	RangedFloat durationPenalty = new RangedFloat().id( "duration_penalty" ).maxRange( Range.of( 0.0f, 1.0f ) );
	RangedFloat amplifier = new RangedFloat().id( "amplifier" ).maxRange( Range.of( 1.0f, 20.0f ) );

	public static ISupplier< AccessoryItem > create( float durationPenalty, float amplifier ) {
		return handler->new StrongerPotions( handler, durationPenalty, amplifier );
	}

	protected StrongerPotions( BonusHandler< AccessoryItem > handler, float durationPenalty, float amplifier ) {
		super( handler );

		this.durationPenalty.set( durationPenalty, Range.of( 0.0f, 1.0f ) );
		this.amplifier.set( amplifier, Range.of( 1.0f, 10.0f ) );

		OnItemBrewed.listen( this::boostPotions )
			.addCondition( data->data.items.subList( 0, 3 ).stream().anyMatch( itemStack->!PotionUtils.getMobEffects( itemStack ).isEmpty() ) );

		this.addTooltip( "majruszsaccessories.bonuses.potion_amplifier", TooltipHelper.asValue( this.amplifier ).scaleOnlyOnDetailed() );
		this.addTooltip( "majruszsaccessories.bonuses.potion_duration", TooltipHelper.asPercent( this.durationPenalty ).bonusMultiplier( -1.0f ) );

		handler.getConfig()
			.define( "stronger_potion", subconfig->{
				this.durationPenalty.define( subconfig );
				this.amplifier.define( subconfig );
			} );
	}

	private void boostPotions( OnItemBrewed data ) {
		Player player = LevelHelper.getNearestPlayer( data.level, data.blockPos, 10.0f );
		AccessoryHolder holder = AccessoryHolders.get( player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		data.mapPotions( potions->{
			float durationMultiplier = 1.0f - holder.apply( this.durationPenalty, -1.0f );
			int extraAmplifier = Math.round( holder.apply( this.amplifier ) );

			return potions.stream()
				.map( itemStack->{
					List< MobEffectInstance > effects = PotionUtils.getMobEffects( itemStack );
					if( effects.isEmpty() ) {
						return itemStack;
					}

					ItemStack potion = new ItemStack( itemStack.getItem() );
					Serializables.write( new Data(), potion.getOrCreateTag() );

					return PotionUtils.setCustomEffects( potion, PotionUtils.getMobEffects( itemStack )
						.stream()
						.map( effect->new MobEffectInstance( effect.getEffect(), Math.max( 40, ( int )( effect.getDuration() * durationMultiplier ) ), effect.getAmplifier() + extraAmplifier ) )
						.toList()
					);
				} )
				.toList();
		} );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnItemBrewed data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 6 )
			.position( AnyPos.from( data.blockPos ).center().vec3() )
			.emit( data.getServerLevel() );
	}

	private static class Data {
		static {
			Serializables.get( Data.class )
				.define( "display", config->{
					config.define( "Name", Reader.string(), s->s.name, ( s, v )->s.name = v );
				} );
		}

		private String name = "{\"translate\":\"majruszsaccessories.bonuses.potion_name\",\"italic\":false}";
	}
}
