package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnFishingTimeGet;
import com.mlib.data.Serializable;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.level.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.core.BlockPos;

public class FishingLureBonus extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 1.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new FishingLureBonus( handler, bonus );
	}

	protected FishingLureBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.multiplier.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnFishingTimeGet.listen( this::decreaseFishingTime )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.player ) );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_lure", TooltipHelper.asPercent( this.multiplier ) );

		Serializable< ? > config = handler.getConfig();
		config.define( "fishing_time", this.multiplier::define );
	}

	private void decreaseFishingTime( OnFishingTimeGet data ) {
		data.time = Math.round( data.time * ( 1.0f - CustomConditions.getLastHolder().apply( this.multiplier ) ) );
		this.spawnEffects( data );
	}

	private void spawnEffects( OnFishingTimeGet data ) {
		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );

		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
