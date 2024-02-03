package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.events.OnEntitySwimSpeedMultiplierGet;
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

public class SwimmingSpeedBonus extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new SwimmingSpeedBonus( handler, bonus );
	}

	protected SwimmingSpeedBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.multiplier.set( bonus, Range.of( 0.0f, 10.0f ) );

		OnEntitySwimSpeedMultiplierGet.listen( this::increaseSwimSpeed );

		this.addTooltip( "majruszsaccessories.bonuses.swim_bonus", TooltipHelper.asPercent( this.multiplier ) );

		handler.getConfig()
			.define( "swim_speed", this.multiplier::define );
	}

	private void increaseSwimSpeed( OnEntitySwimSpeedMultiplierGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.entity ).get( this::getItem );
		if( !holder.isValid() ) {
			return;
		}

		data.multiplier *= 1.0f + holder.apply( this.multiplier );
		if( data.entity.isInWater() && data.getLevel() instanceof ServerLevel && TimeHelper.haveTicksPassed( 5 ) ) {
			this.spawnEffects( data, holder );
		}
	}

	private void spawnEffects( OnEntitySwimSpeedMultiplierGet data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 1 )
			.sizeBased( data.entity )
			.emit( data.getServerLevel() );
	}
}
