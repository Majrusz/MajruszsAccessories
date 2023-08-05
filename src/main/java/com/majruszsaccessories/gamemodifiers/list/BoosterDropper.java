package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.Registries;
import com.mlib.modhelper.AutoInstance;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.ModConfigs;
import com.mlib.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import com.mlib.loot.LootHelper;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

@AutoInstance
public class BoosterDropper {
	static final ResourceLocation LOOT_TABLE = Registries.getLocation( "gameplay/nether_accessories" );

	public BoosterDropper() {
		ConfigGroup group = ModConfigs.getGroup( Registries.Groups.BOOSTERS );

		DoubleConfig chestChance = new DoubleConfig( 0.1, Range.CHANCE );
		chestChance.name( "chest_chance" ).comment( "Chance for random booster to spawn in any Nether chest." );

		OnLoot.listen( this::spawnBoosterInChest )
			.addCondition( Condition.isServer() )
			.addCondition( Condition.chance( chestChance ) )
			.addCondition( OnLoot.hasOrigin() )
			.addCondition( Condition.predicate( data->BlockHelper.getBlockEntity( data.getLevel(), data.origin ) instanceof RandomizableContainerBlockEntity ) )
			.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) )
			.addCondition( Condition.predicate( data->LevelHelper.isEntityIn( data.entity, Level.NETHER ) ) )
			.insertTo( group );

		DoubleConfig mobChance = new DoubleConfig( 0.001, Range.CHANCE );
		mobChance.name( "mob_chance" ).comment( "Chance for random booster to drop from any Nether mob." );

		OnLoot.listen( this::dropBoosterFromMonster )
			.addCondition( Condition.isServer() )
			.addCondition( Condition.chance( mobChance ) )
			.addCondition( OnLoot.hasEntity() )
			.addCondition( OnLoot.hasKiller() )
			.addCondition( OnLoot.hasLastDamagePlayer() )
			.addCondition( Condition.predicate( data->LevelHelper.isEntityIn( data.killer, Level.NETHER ) ) )
			.insertTo( group );
	}

	private void spawnBoosterInChest( OnLoot.Data data ) {
		this.addBooster( data, data.entity );
	}

	private void dropBoosterFromMonster( OnLoot.Data data ) {
		this.addBooster( data, data.killer );
	}

	private void addBooster( OnLoot.Data data, Entity entity ) {
		LootHelper.getLootTable( LOOT_TABLE )
			.getRandomItems( LootHelper.toGiftParams( entity ) )
			.forEach( data.generatedLoot::add );
	}
}
