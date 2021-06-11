package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.Random;
import com.mlib.WorldHelper;
import com.mlib.config.DoubleConfig;
import com.mlib.events.AnyLootModificationEvent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;

/** Rock that gives a chance for extra drops from stone. */
@Mod.EventBusSubscriber
public class LuckyRockItem extends AccessoryItem {
	private static final ResourceLocation LOOT_TABLE_LOCATION = new ResourceLocation( MajruszsAccessories.MOD_ID, "gameplay/lucky_rock" );
	private static final ResourceLocation LOOT_TABLE_THE_NETHER_LOCATION = new ResourceLocation( MajruszsAccessories.MOD_ID,
		"gameplay/lucky_rock_the_nether"
	);
	private static final ResourceLocation LOOT_TABLE_THE_END_LOCATION = new ResourceLocation( MajruszsAccessories.MOD_ID,
		"gameplay/lucky_rock_the_end"
	);
	protected final DoubleConfig dropChance;
	protected final IntegrationDoubleConfig chance;
	protected final DoubleConfig endShardChance;
	protected final DoubleConfig endIngotChance;

	public LuckyRockItem() {
		super( "Lucky Rock", "lucky_rock" );

		String dropComment = "Chance for Lucky Rock to drop from mining.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.0002, 0.0, 1.0 );

		String chanceComment = "Chance for extra loot when mining.";
		this.chance = new IntegrationDoubleConfig( "Chance", chanceComment, 0.03, 0.045, 0.06, 0.0, 1.0 );

		String shardComment = "Chance for End Shard to drop from mining End Stone. (integration with Majrusz's Progressive Difficulty mod)";
		this.endShardChance = new DoubleConfig( "end_shard_chance", shardComment, false, 0.1, 0.0, 1.0 );

		String ingotComment = "Chance for End Ingot to drop from mining End Stone. (integration with Majrusz's Progressive Difficulty mod)";
		this.endIngotChance = new DoubleConfig( "end_ingot_chance", ingotComment, false, 0.01, 0.0, 1.0 );

		this.group.addConfigs( this.dropChance, this.chance );
		if( Integration.isProgressiveDifficultyInstalled() )
			this.group.addConfigs( this.endShardChance, this.endIngotChance );
	}

	/** Generating loot context. (who has Lucky Rock, where is, etc.) */
	protected static LootContext generateLootContext( PlayerEntity player ) {
		LootContext.Builder lootContextBuilder = new LootContext.Builder( ( ServerWorld )player.getCommandSenderWorld() );
		lootContextBuilder.withParameter( LootParameters.ORIGIN, player.position() );
		lootContextBuilder.withParameter( LootParameters.THIS_ENTITY, player );

		return lootContextBuilder.create( LootParameterSets.GIFT );
	}

	@SubscribeEvent
	public static void onGeneratingLoot( AnyLootModificationEvent event ) {
		if( event.origin == null || event.blockState == null || event.tool == null || !( event.entity instanceof PlayerEntity ) || !( event.entity.getCommandSenderWorld() instanceof ServerWorld ) )
			return;

		boolean isRock = event.blockState.getMaterial() == Material.STONE;
		boolean wasMinedWithPickaxe = event.tool.getItem() instanceof PickaxeItem;
		if( !( isRock && wasMinedWithPickaxe ) )
			return;

		LuckyRockItem luckyRock = Instances.LUCKY_ROCK_ITEM;
		PlayerEntity player = ( PlayerEntity )event.entity;
		ServerWorld world = ( ServerWorld )player.getCommandSenderWorld();

		if( luckyRock.hasAny( player ) && Random.tryChance( luckyRock.getExtraLootChance( player ) ) ) {
			event.generatedLoot.addAll( luckyRock.generateLoot( player ) );
			luckyRock.spawnParticles( event.origin, world, 0.375 );
		}

		if( Random.tryChance( luckyRock.getDropChance() ) ) {
			ItemStack itemStack = new ItemStack( luckyRock, 1 );
			luckyRock.setRandomEffectiveness( itemStack );

			event.generatedLoot.add( itemStack );
		}
	}

	/** Returns current chance for extra loot from mining. */
	public double getExtraLootChance( PlayerEntity player ) {
		return MathHelper.clamp( this.chance.getValue() * ( 1.0 + getHighestEffectiveness( player ) ), 0.0, 1.0 );
	}

	/** Returns a chance for Lucky Rock to drop. */
	public double getDropChance() {
		return this.dropChance.get();
	}

	/** Generating random loot from Lucky Rock's loot table. */
	public List< ItemStack > generateLoot( PlayerEntity player ) {
		LootTable lootTable = getLootTable( player );
		List< ItemStack > generatedLoot = lootTable.getRandomItems( generateLootContext( player ) );
		addIntegrationLoot( generatedLoot, player );

		return generatedLoot;
	}

	/** Returning loot table for Lucky Rock. (possible loot) */
	protected LootTable getLootTable( PlayerEntity player ) {
		return ServerLifecycleHooks.getCurrentServer()
			.getLootTables()
			.get( getLootTableLocation( player ) );
	}

	/** Returns loot table location depending on player's dimension. */
	private ResourceLocation getLootTableLocation( PlayerEntity player ) {
		if( WorldHelper.isEntityIn( player, World.NETHER ) )
			return LOOT_TABLE_THE_NETHER_LOCATION;
		else if( WorldHelper.isEntityIn( player, World.END ) )
			return LOOT_TABLE_THE_END_LOCATION;

		return LOOT_TABLE_LOCATION;
	}

	/** Adds extra loot with random chance to generated loot if Majrusz's Progressive Difficulty mod is installed. */
	private void addIntegrationLoot( List< ItemStack > generatedLoot, PlayerEntity player ) {
		if( !( Integration.isProgressiveDifficultyInstalled() && WorldHelper.isEntityIn( player, World.END ) ) )
			return;

		if( Random.tryChance( this.endShardChance.get() ) )
			generatedLoot.add( new ItemStack( com.majruszs_difficulty.Instances.END_SHARD_ITEM ) );

		if( Random.tryChance( this.endIngotChance.get() ) )
			generatedLoot.add( new ItemStack( com.majruszs_difficulty.Instances.END_INGOT_ITEM ) );
	}
}
