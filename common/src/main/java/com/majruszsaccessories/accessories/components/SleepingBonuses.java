package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnPlayerWakedUp;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.math.Random;
import com.mlib.math.Range;
import com.mlib.time.TimeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class SleepingBonuses extends BonusComponent< AccessoryItem > {
	RangedInteger count = new RangedInteger().id( "count" ).maxRange( Range.of( 1, 100 ) );
	RangedInteger duration = new RangedInteger().id( "duration" ).maxRange( Range.of( 1, 10000 ) );
	List< EffectDef > effects = List.of(
		new EffectDef( MobEffects.REGENERATION, 0 ),
		new EffectDef( MobEffects.SATURATION, 0 ),
		new EffectDef( MobEffects.ABSORPTION, 1 ),
		new EffectDef( MobEffects.DAMAGE_RESISTANCE, 0 ),
		new EffectDef( MobEffects.FIRE_RESISTANCE, 0 ),
		new EffectDef( MobEffects.MOVEMENT_SPEED, 0 )
	);

	public static ISupplier< AccessoryItem > create( int count, int duration ) {
		return handler->new SleepingBonuses( handler, count, duration );
	}

	protected SleepingBonuses( BonusHandler< AccessoryItem > handler, int count, int duration ) {
		super( handler );

		this.count.set( count, Range.of( 1, 10 ) );
		this.duration.set( duration, Range.of( 1, 10000 ) );

		OnPlayerWakedUp.listen( this::applyBonuses )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( data->!data.wasSleepStoppedManually )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.player ) );

		this.addTooltip( "majruszsaccessories.bonuses.sleep_bonuses", TooltipHelper.asValue( this.count ), TooltipHelper.asValue( this.duration ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "sleep_bonuses", subconfig->{
			this.count.define( subconfig );
			this.duration.define( subconfig );
			subconfig.defineCustom( "effects", ()->this.effects, x->this.effects = x, EffectDef::new );
		} );
	}

	private void applyBonuses( OnPlayerWakedUp data ) {
		AccessoryHolder holder = CustomConditions.getLastHolder();
		int count = holder.apply( this.count );
		int duration = TimeHelper.toTicks( holder.apply( this.duration ) );
		this.getRandomMobEffects( data.player, count )
			.forEach( effect->data.player.addEffect( new MobEffectInstance( effect.effect, duration, effect.amplifier ) ) );

		this.spawnEffects( data );
	}

	private List< EffectDef > getRandomMobEffects( Player player, int count ) {
		List< EffectDef > missingEffects = this.effects.stream().filter( effect->!player.hasEffect( effect.effect ) ).toList();
		if( missingEffects.isEmpty() ) {
			missingEffects = this.effects;
		}

		return Random.next( missingEffects, count );
	}

	private void spawnEffects( OnPlayerWakedUp data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 5 )
			.position( data.player.position() )
			.emit( data.getServerLevel() );
	}

	private static class EffectDef extends Serializable {
		private MobEffect effect;
		private int amplifier;

		public EffectDef() {
			this.defineEffect( "id", ()->this.effect, x->this.effect = x );
			this.defineInteger( "amplifier", ()->this.amplifier, x->this.amplifier = x );
		}

		public EffectDef( MobEffect effect, int amplifier ) {
			this();

			this.effect = effect;
			this.amplifier = amplifier;
		}
	}
}
