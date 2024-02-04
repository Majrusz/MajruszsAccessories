package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.events.OnBreakSpeedGet;
import com.majruszlibrary.events.OnItemSwingDurationGet;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszlibrary.time.TimeHelper;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.server.level.ServerLevel;

public class MiningSpeedBonus extends BonusComponent< AccessoryItem > {
	RangedFloat speedMultiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new MiningSpeedBonus( handler, bonus );
	}

	protected MiningSpeedBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.speedMultiplier.set( bonus, Range.of( 0.0f, 10.0f ) );

		OnBreakSpeedGet.listen( this::increaseMineSpeed );

		OnItemSwingDurationGet.listen( this::decreaseSwingDuration );

		this.addTooltip( "majruszsaccessories.bonuses.mine_bonus", TooltipHelper.asPercent( this.speedMultiplier ) );

		handler.getConfig()
			.define( "mining_speed_bonus", this.speedMultiplier::define );
	}

	private void increaseMineSpeed( OnBreakSpeedGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		data.speed += data.original * holder.apply( this.speedMultiplier );
		if( data.getLevel() instanceof ServerLevel && TimeHelper.haveTicksPassed( 10 ) ) {
			this.spawnEffects( data, holder );
		}
	}

	private void decreaseSwingDuration( OnItemSwingDurationGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.entity ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		float bonus = holder.apply( this.speedMultiplier );
		data.duration -= Random.round( data.original * bonus / ( 1.0f + bonus ) );
	}

	private void spawnEffects( OnBreakSpeedGet data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 1 )
			.sizeBased( data.player )
			.emit( data.getServerLevel() );
	}
}
