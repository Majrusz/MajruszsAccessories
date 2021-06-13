package com.majruszsaccessories.items;

import com.majruszs_difficulty.events.TreasureBagOpenedEvent;
import com.majruszsaccessories.Instances;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.majruszsaccessories.config.IntegrationIntegerConfig;
import com.mlib.MajruszLibrary;
import com.mlib.Random;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.DoubleConfig;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.075/20.0, 0.0, 1.0 );

		this.group.addConfig( this.luck );
		if( !Integration.isProgressiveDifficultyInstalled() )
			this.group.addConfig( this.dropChance );
	}

	@SubscribeEvent
	public static void increaseLuck( TickEvent.PlayerTickEvent event ) {
		PlayerEntity player = event.player;

		LUCK_ATTRIBUTE.setValueAndApply( player, Instances.FISHERMAN_EMBLEM_ITEM.getEmblemLuckBonus( player ) );
	}

	@SubscribeEvent
	public static void onFishing( ItemFishedEvent event ) {
		PlayerEntity player = event.getPlayer();
		if( !( player.getCommandSenderWorld() instanceof ServerWorld ) || Integration.isProgressiveDifficultyInstalled() || player.fishing == null )
			return;

		if( Random.tryChance( Instances.FISHERMAN_EMBLEM_ITEM.dropChance.get() ) )
			spawnEmblem( ( ServerWorld )player.getCommandSenderWorld(), player.fishing, player );
	}

	/** Adds Fisherman Emblem to Fisherman Treasure Bag. (if Majrusz's Progressive Difficulty is installed) */
	public static void addToTreasureBag( TreasureBagOpenedEvent event ) {
		if( event.treasureBagItem.equals( com.majruszs_difficulty.Instances.FISHING_TREASURE_BAG ) && Random.tryChance( 0.075 ) )
			event.generatedLoot.add( Instances.FISHERMAN_EMBLEM_ITEM.getRandomInstance() );
	}

	/** Returns current luck bonus. (whether player has emblem and is fishing or not) */
	public int getEmblemLuckBonus( PlayerEntity player ) {
		return player.fishing != null && hasAny( player ) ? getLuckBonus( player ) : 0;
	}

	/** Returns total luck bonus. */
	public int getLuckBonus( PlayerEntity player ) {
		return ( int )Math.round( this.luck.getValue() * ( 1.0 + getHighestEffectiveness( player ) ) );
	}

	/** Spawns Fisherman Emblem in game world. */
	private static void spawnEmblem( ServerWorld world, FishingBobberEntity bobberEntity, PlayerEntity player ) {
		double x = bobberEntity.getX(), y = bobberEntity.getY(), z = bobberEntity.getZ();
		Vector3d position = Random.getRandomVector3d( x-0.25, x+0.25, y+0.125, y+0.5, z-0.25, z+0.25 );
		ItemEntity itemEntity = new ItemEntity( world, position.x, position.y, position.z, Instances.FISHERMAN_EMBLEM_ITEM.getRandomInstance() );

		Vector3d delta = player.position().subtract( position );
		itemEntity.lerpMotion( 0.1 * delta.x,
			0.1 * delta.x + Math.pow( Math.pow( delta.x, 2 ) + Math.pow( delta.y, 2 ) + Math.pow( delta.z, 2 ), 0.25 ) * 0.08, 0.1 * delta.z
		);

		world.addFreshEntity( itemEntity );
	}
}
