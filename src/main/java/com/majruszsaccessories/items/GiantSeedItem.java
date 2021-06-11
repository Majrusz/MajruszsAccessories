package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.events.HarvestCropEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
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
		PlayerEntity player = event.getPlayer();

		if( !( player.getCommandSenderWorld() instanceof ServerWorld && event.crops.isMaxAge( event.blockState ) ) )
			return;

		if( Random.tryChance( giantSeed.getDropChance() ) ) {
			ItemStack itemStack = new ItemStack( giantSeed, 1 );
			giantSeed.setRandomEffectiveness( itemStack );

			event.generatedLoot.add( itemStack );
		}

		if( giantSeed.hasAny( player ) && Random.tryChance( giantSeed.getDoubleLootChance( player ) ) ) {
			List< ItemStack > extraItems = new ArrayList<>( event.generatedLoot );
			extraItems.removeIf( itemStack->itemStack.getItem() instanceof GiantSeedItem );

			event.generatedLoot.addAll( extraItems );
			giantSeed.spawnParticles( event.origin, ( ServerWorld )player.getCommandSenderWorld(), 0.25 );
		}
	}

	/** Returns current chance for double crops. */
	public double getDoubleLootChance( PlayerEntity player ) {
		return MathHelper.clamp( this.chance.getValue() * ( 1.0 + getHighestEffectiveness( player ) ), 0.0, 1.0 );
	}

	/** Returns a chance for Giant Seed to drop. */
	public double getDropChance() {
		return this.dropChance.get();
	}
}
