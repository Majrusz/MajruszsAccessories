package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.LevelHelper;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.events.AnyLootModificationEvent;
import com.mlib.particles.ParticleHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

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
	protected static LootContext generateLootContext( Player player ) {
		LootContext.Builder lootContextBuilder = new LootContext.Builder( ( ServerLevel )player.getCommandSenderWorld() );
		lootContextBuilder.withParameter( LootContextParams.ORIGIN, player.position() );
		lootContextBuilder.withParameter( LootContextParams.THIS_ENTITY, player );

		return lootContextBuilder.create( LootContextParamSets.GIFT );
	}

	@SubscribeEvent
	public static void onGeneratingLoot( AnyLootModificationEvent event ) {
		if( event.origin == null || event.blockState == null || event.tool == null || !( event.entity instanceof Player ) || !( event.entity.getCommandSenderWorld() instanceof ServerLevel ) )
			return;

		boolean isRock = event.blockState.getMaterial() == Material.STONE;
		if( !( isRock && isPickaxe( event.tool.getItem() ) ) )
			return;

		LuckyRockItem luckyRock = Instances.LUCKY_ROCK_ITEM;
		Player player = ( Player )event.entity;
		ServerLevel level = ( ServerLevel )player.getCommandSenderWorld();

		if( luckyRock.hasAny( player ) && Random.tryChance( luckyRock.getExtraLootChance( player ) ) ) {
			event.generatedLoot.addAll( luckyRock.generateLoot( player ) );
			ParticleHelper.spawnAwardParticles( level, event.origin, 5, 0.375 );
		}

		if( Random.tryChance( luckyRock.dropChance.get() ) )
			event.generatedLoot.add( luckyRock.getRandomInstance() );
	}

	/** Checks whether given item is a pickaxe. (or something from other mods that is variant of pickaxe) */
	private static boolean isPickaxe( Item item ) {
		if( item.getRegistryName() == null )
			return false;

		String registryName = item.getRegistryName()
			.toString();

		return registryName.contains( "pickaxe" ) || registryName.contains( "hammer" );
	}

	/** Returns current chance for extra loot from mining. */
	public double getExtraLootChance( Player player ) {
		return Mth.clamp( this.chance.getValue() * ( 1.0 + getHighestEffectiveness( player ) ), 0.0, 1.0 );
	}

	/** Generating random loot from Lucky Rock's loot table. */
	public List< ItemStack > generateLoot( Player player ) {
		LootTable lootTable = getLootTable( player );
		List< ItemStack > generatedLoot = lootTable.getRandomItems( generateLootContext( player ) );
		addIntegrationLoot( generatedLoot, player );

		return generatedLoot;
	}

	/** Returning loot table for Lucky Rock. (possible loot) */
	protected LootTable getLootTable( Player player ) {
		return ServerLifecycleHooks.getCurrentServer()
			.getLootTables()
			.get( getLootTableLocation( player ) );
	}

	/** Returns loot table location depending on player's dimension. */
	private ResourceLocation getLootTableLocation( Player player ) {
		if( LevelHelper.isEntityIn( player, Level.NETHER ) )
			return LOOT_TABLE_THE_NETHER_LOCATION;
		else if( LevelHelper.isEntityIn( player, Level.END ) )
			return LOOT_TABLE_THE_END_LOCATION;

		return LOOT_TABLE_LOCATION;
	}

	/** Adds extra loot with random chance to generated loot if Majrusz's Progressive Difficulty mod is installed. */
	private void addIntegrationLoot( List< ItemStack > generatedLoot, Player player ) {
		if( !( Integration.isProgressiveDifficultyInstalled() && LevelHelper.isEntityIn( player, Level.END ) ) )
			return;

		if( Random.tryChance( this.endShardChance.get() ) )
			generatedLoot.add( new ItemStack( com.majruszs_difficulty.Instances.END_SHARD_ITEM ) );

		if( Random.tryChance( this.endIngotChance.get() ) )
			generatedLoot.add( new ItemStack( com.majruszs_difficulty.Instances.END_INGOT_ITEM ) );
	}
}
