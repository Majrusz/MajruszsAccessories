package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.boosters.BoosterItem;
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

public class LowerSpawnRate extends BoosterComponent {
	final DoubleConfig spawnRateReduction;
	float currentMultiplier = 1.0f;

	public static ISupplier create( double chance ) {
		return ( item, group )->new LowerSpawnRate( item, group, chance );
	}

	protected LowerSpawnRate( Supplier< BoosterItem > item, ConfigGroup group, double spawnRateReduction ) {
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

		this.addTooltip( "majruszsaccessories.boosters.lower_spawn_rate", TooltipHelper.asItem( item ), TooltipHelper.asFixedPercent( this.spawnRateReduction ) );
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
			long boostersCount = server.getPlayerList().getPlayers().stream().filter( player->AccessoryHolder.hasBooster( player, this.item.get() ) ).count();

			this.currentMultiplier = ( float )Math.pow( 1.0f - this.spawnRateReduction.get(), boostersCount );
		} else {
			this.currentMultiplier = 1.0f;
		}
	}
}
