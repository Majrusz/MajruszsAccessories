package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnMobSpawnLimitGet;
import com.mlib.contexts.OnMobSpawnRateGet;
import com.mlib.contexts.OnServerTicked;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import com.mlib.platform.Side;
import net.minecraft.world.entity.MobCategory;

public class LowerSpawnRate extends BonusComponent< AccessoryItem > {
	RangedFloat reduction = new RangedFloat().id( "reduction" ).maxRange( Range.of( 0.0f, 1.0f ) );
	float currentMultiplier = 1.0f;

	public static ISupplier< AccessoryItem > create( float reduction ) {
		return handler->new LowerSpawnRate( handler, reduction );
	}

	protected LowerSpawnRate( BonusHandler< AccessoryItem > handler, float reduction ) {
		super( handler );

		this.reduction.set( reduction, Range.of( 0.0f, 1.0f ) );

		OnMobSpawnRateGet.listen( this::decreaseSpawnRate )
			.addCondition( data->data.category.equals( MobCategory.MONSTER ) );

		OnMobSpawnLimitGet.listen( this::decreaseSpawnLimit )
			.addCondition( data->data.category.equals( MobCategory.MONSTER ) );

		OnServerTicked.listen( this::updateSpawnMultiplier )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.cooldown( 1.0f ) );

		this.addTooltip( "majruszsaccessories.bonuses.lower_spawn_rate", TooltipHelper.asPercent( this.reduction ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "spawn_rate", this.reduction::define );
	}

	private void decreaseSpawnRate( OnMobSpawnRateGet data ) {
		data.value *= this.currentMultiplier;
	}

	private void decreaseSpawnLimit( OnMobSpawnLimitGet data ) {
		data.value *= this.currentMultiplier;
	}

	private void updateSpawnMultiplier( OnServerTicked data ) {
		this.currentMultiplier = Side.getServer().getPlayerList()
			.getPlayers()
			.stream()
			.map( player->{
				AccessoryHolder holder = AccessoryHolder.find( player, this.getItem() );

				return holder.isValid() ? 1.0f - holder.apply( this.reduction ) : 1.0f;
			} )
			.reduce( 1.0f, ( total, multiplier )->total * multiplier );
	}
}
