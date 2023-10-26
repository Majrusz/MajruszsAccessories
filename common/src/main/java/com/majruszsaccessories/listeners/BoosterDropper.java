package com.majruszsaccessories.listeners;

import com.majruszsaccessories.MajruszsAccessories;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.entity.EntityHelper;
import com.mlib.item.LootHelper;
import com.mlib.level.BlockHelper;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

@AutoInstance
public class BoosterDropper {
	private ResourceLocation lootTable = MajruszsAccessories.HELPER.getLocation( "gameplay/nether_accessories" );
	private float chestChance = 0.1f;
	private float mobChance = 0.005f;

	public BoosterDropper() {
		OnLootGenerated.listen( this::spawnBoosterInChest )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.hasLevel() )
			.addCondition( Condition.chance( ()->this.chestChance ) )
			.addCondition( Condition.predicate( data->data.origin != null ) )
			.addCondition( Condition.predicate( data->BlockHelper.getEntity( data.getLevel(), data.origin ) instanceof RandomizableContainerBlockEntity ) )
			.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) )
			.addCondition( Condition.predicate( data->EntityHelper.isIn( data.entity, Level.NETHER ) ) );

		OnLootGenerated.listen( this::dropBoosterFromMonster )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.hasLevel() )
			.addCondition( Condition.chance( ()->this.mobChance ) )
			.addCondition( Condition.predicate( data->data.entity != null ) )
			.addCondition( Condition.predicate( data->data.killer != null ) )
			.addCondition( Condition.predicate( data->data.lastDamagePlayer != null ) )
			.addCondition( Condition.predicate( data->EntityHelper.isIn( data.killer, Level.NETHER ) ) );

		MajruszsAccessories.CONFIG.boosters.defineLocation( "loot_table", ()->this.lootTable, x->this.lootTable = x );
		MajruszsAccessories.CONFIG.boosters.defineFloat( "nether_chest_chance", ()->this.chestChance, x->this.chestChance = Range.CHANCE.clamp( x ) );
		MajruszsAccessories.CONFIG.boosters.defineFloat( "nether_mob_chance", ()->this.mobChance, x->this.mobChance = Range.CHANCE.clamp( x ) );
	}

	private void spawnBoosterInChest( OnLootGenerated data ) {
		this.addBooster( data, data.entity );
	}

	private void dropBoosterFromMonster( OnLootGenerated data ) {
		this.addBooster( data, data.killer );
	}

	private void addBooster( OnLootGenerated data, Entity entity ) {
		LootHelper.getLootTable( this.lootTable )
			.getRandomItems( LootHelper.toGiftParams( entity ) )
			.forEach( data.generatedLoot::add );
	}
}
