package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnMobSpawnLimit;
import com.mlib.contexts.OnMobSpawnRate;
import com.mlib.contexts.OnServerTick;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class LowerSpawnRate extends AccessoryComponent {
	final DoubleConfig spawnRateReduction;
	float currentMultiplier = 1.0f;

	public static ISupplier create( double chance ) {
		return ( item, group )->new LowerSpawnRate( item, group, chance );
	}

	protected LowerSpawnRate( Supplier< AccessoryItem > item, ConfigGroup group, double spawnRateReduction ) {
		super( item );

		this.spawnRateReduction = new DoubleConfig( spawnRateReduction, new Range<>( 0.01, 0.99 ) );
		group.addConfig( this.spawnRateReduction.name( "spawn_rate_reduction" ).comment( "Decreases spawn rate of all hostile mobs for all dimensions." ) );

		OnMobSpawnRate.listen( this::decreaseSpawnRate )
			.addCondition( OnMobSpawnRate.is( MobCategory.MONSTER ) )
			.insertTo( group );

		OnMobSpawnLimit.listen( this::decreaseSpawnLimit )
			.addCondition( OnMobSpawnLimit.is( MobCategory.MONSTER ) )
			.insertTo( group );

		OnServerTick.listen( this::updateSpawnMultiplier )
			.addCondition( Condition.cooldown( 1.0, Dist.DEDICATED_SERVER ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.lower_spawn_rate", TooltipHelper.asPercent( this.spawnRateReduction ) );
	}

	private void decreaseSpawnRate( OnMobSpawnRate.Data data ) {
		data.value *= this.currentMultiplier;
	}

	private void decreaseSpawnLimit( OnMobSpawnLimit.Data data ) {
		data.value *= this.currentMultiplier;
	}

	private void updateSpawnMultiplier( OnServerTick.Data data ) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if( server != null ) {
			this.currentMultiplier = server.getPlayerList()
				.getPlayers()
				.stream()
				.map( player->{
					AccessoryHolder holder = AccessoryHolder.find( player, this.item.get() );

					return holder.isValid() ? 1.0f - holder.apply( this.spawnRateReduction ) : 1.0f;
				} )
				.reduce( 1.0f, ( total, multiplier )->total * multiplier );
		} else {
			this.currentMultiplier = 1.0f;
		}
	}
}
