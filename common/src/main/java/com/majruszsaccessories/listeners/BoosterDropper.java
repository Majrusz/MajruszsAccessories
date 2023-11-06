package com.majruszsaccessories.listeners;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.config.Config;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializables;
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

		Serializables.get( Config.Boosters.class )
			.defineLocation( "loot_table", s->this.lootTable, ( s, v )->this.lootTable = v )
			.defineFloat( "nether_chest_chance", s->this.chestChance, ( s, v )->this.chestChance = Range.CHANCE.clamp( v ) )
			.defineFloat( "nether_mob_chance", s->this.mobChance, ( s, v )->this.mobChance = Range.CHANCE.clamp( v ) );
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
