package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnBreakSpeedGet;
import com.mlib.contexts.OnItemSwingDurationGet;
import com.mlib.data.Serializable;
import com.mlib.math.Random;
import com.mlib.math.Range;
import com.mlib.time.TimeHelper;
import net.minecraft.server.level.ServerLevel;

public class MiningSpeedBonus extends BonusComponent< AccessoryItem > {
	RangedFloat speedMultiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new MiningSpeedBonus( handler, bonus );
	}

	protected MiningSpeedBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.speedMultiplier.set( bonus, Range.of( 0.0f, 10.0f ) );

		OnBreakSpeedGet.listen( this::increaseMineSpeed )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.player ) );

		OnItemSwingDurationGet.listen( this::decreaseSwingDuration )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.entity ) );

		this.addTooltip( "majruszsaccessories.bonuses.mine_bonus", TooltipHelper.asPercent( this.speedMultiplier ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "mining_speed_bonus", this.speedMultiplier::define );
	}

	private void increaseMineSpeed( OnBreakSpeedGet data ) {
		data.speed += data.original * CustomConditions.getLastHolder().apply( this.speedMultiplier );
		if( data.getLevel() instanceof ServerLevel && TimeHelper.haveTicksPassed( 10 ) ) {
			this.spawnEffects( data );
		}
	}

	private void decreaseSwingDuration( OnItemSwingDurationGet data ) {
		float bonus = CustomConditions.getLastHolder().apply( this.speedMultiplier );

		data.duration -= Random.round( data.original * bonus / ( 1.0f + bonus ) );
	}

	private void spawnEffects( OnBreakSpeedGet data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 1 )
			.sizeBased( data.player )
			.emit( data.getServerLevel() );
	}
}
