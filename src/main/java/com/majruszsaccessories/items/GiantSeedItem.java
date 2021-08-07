package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.events.HarvestCropEvent;
import com.mlib.particles.ParticleHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/** Seed that gives a chance for double loot from crops. */
@Mod.EventBusSubscriber
public class GiantSeedItem extends AccessoryItem {
	protected final DoubleConfig dropChance;
	protected final IntegrationDoubleConfig chance;

	public GiantSeedItem() {
		super( "Giant Seed", "giant_seed" );

		String dropComment = "Chance for Giant Seed to drop from harvesting.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.001, 0.0, 1.0 );

		String chanceComment = "Chance for double loot when harvesting crops.";
		this.chance = new IntegrationDoubleConfig( "Chance", chanceComment, 0.25, 0.4, 0.55, 0.0, 1.0 );

		this.group.addConfigs( this.dropChance, this.chance );
	}

	@SubscribeEvent
	public static void handleHarvesting( HarvestCropEvent event ) {
		GiantSeedItem giantSeed = Instances.GIANT_SEED_ITEM;
		Player player = event.getPlayer();

		if( !( player.level instanceof ServerLevel && event.crops.isMaxAge( event.blockState ) ) )
			return;

		if( giantSeed.hasAny( player ) && Random.tryChance( giantSeed.getDoubleLootChance( player ) ) ) {
			List< ItemStack > extraItems = new ArrayList<>( event.generatedLoot );

			event.generatedLoot.addAll( extraItems );
			ParticleHelper.spawnAwardParticles( ( ServerLevel )player.level, event.origin, 5, 0.25 );
		}

		if( Random.tryChance( giantSeed.dropChance.get() ) )
			event.generatedLoot.add( giantSeed.getRandomInstance() );
	}

	/** Returns current chance for double crops. */
	public double getDoubleLootChance( Player player ) {
		return Mth.clamp( this.chance.getValue() * ( 1.0 + getHighestEffectiveness( player ) ), 0.0, 1.0 );
	}
}
