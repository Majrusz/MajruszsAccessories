package com.majruszsaccessories.items;

import com.majruszs_difficulty.events.TreasureBagOpenedEvent;
import com.majruszsaccessories.Instances;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.config.IntegrationIntegerConfig;
import com.mlib.LevelHelper;
import com.mlib.Random;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.DoubleConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Emblem that increases luck for the player that is currently fishing. */
@Mod.EventBusSubscriber
public class FishermanEmblemItem extends AccessoryItem {
	protected static final AttributeHandler LUCK_ATTRIBUTE = new AttributeHandler( "4010270c-9d57-4273-8a41-00985f1e4781", "FishermanEmblemLuckBonus",
		Attributes.LUCK, AttributeModifier.Operation.ADDITION
	);
	protected final IntegrationIntegerConfig luck;
	protected final DoubleConfig dropChance;

	public FishermanEmblemItem() {
		super( "Fisherman Emblem", "fisherman_emblem" );

		String luckComment = "Luck bonus when fishing.";
		this.luck = new IntegrationIntegerConfig( "Luck", luckComment, 3, 4, 5, 1, 100 );

		String dropComment = "Chance for Fisherman Emblem to drop from fishing.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.075 / 20.0, 0.0, 1.0 );

		this.group.addConfig( this.luck );
		if( !Integration.isProgressiveDifficultyInstalled() )
			this.group.addConfig( this.dropChance );
	}

	@SubscribeEvent
	public static void increaseLuck( TickEvent.PlayerTickEvent event ) {
		Player player = event.player;

		LUCK_ATTRIBUTE.setValueAndApply( player, Instances.FISHERMAN_EMBLEM_ITEM.getEmblemLuckBonus( player ) );
	}

	@SubscribeEvent
	public static void onFishing( ItemFishedEvent event ) {
		Player player = event.getPlayer();
		if( !( player.level instanceof ServerLevel ) || Integration.isProgressiveDifficultyInstalled() || player.fishing == null )
			return;

		if( Random.tryChance( Instances.FISHERMAN_EMBLEM_ITEM.dropChance.get() ) )
			spawnEmblem( ( ServerLevel )player.level, player.fishing, player );
	}

	/** Adds Fisherman Emblem to Fisherman Treasure Bag. (if Majrusz's Progressive Difficulty is installed) */
	public static void addToTreasureBag( Event event ) {
		if( !( event instanceof TreasureBagOpenedEvent ) )
			return;

		TreasureBagOpenedEvent bagOpenedEvent = ( TreasureBagOpenedEvent )event;
		if( bagOpenedEvent.treasureBagItem.equals( com.majruszs_difficulty.Instances.FISHING_TREASURE_BAG ) && Random.tryChance( 0.075 ) )
			bagOpenedEvent.generatedLoot.add( Instances.FISHERMAN_EMBLEM_ITEM.getRandomInstance() );
	}

	/** Spawns Fisherman Emblem in game world. */
	private static void spawnEmblem( ServerLevel level, FishingHook fishingHook, Player player ) {
		ItemStack fishermanEmblem = Instances.FISHERMAN_EMBLEM_ITEM.getRandomInstance();
		LevelHelper.spawnItemEntityFlyingTowardsDirection( fishermanEmblem, level, fishingHook.position(), player.position() );
	}

	/** Returns current luck bonus. (whether player has emblem and is fishing or not) */
	public int getEmblemLuckBonus( Player player ) {
		return player.fishing != null && hasAny( player ) ? getLuckBonus( player ) : 0;
	}

	/** Returns total luck bonus. */
	public int getLuckBonus( Player player ) {
		return ( int )Math.round( this.luck.getValue() * ( 1.0 + getHighestEffectiveness( player ) ) );
	}
}
