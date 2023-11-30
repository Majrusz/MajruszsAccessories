package com.majruszsaccessories.listeners;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.entity.EntityHelper;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.item.LootHelper;
import com.majruszlibrary.level.BlockHelper;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.config.Config;
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
			.define( "loot_table", Reader.location(), s->this.lootTable, ( s, v )->this.lootTable = v )
			.define( "nether_chest_chance", Reader.number(), s->this.chestChance, ( s, v )->this.chestChance = Range.CHANCE.clamp( v ) )
			.define( "nether_mob_chance", Reader.number(), s->this.mobChance, ( s, v )->this.mobChance = Range.CHANCE.clamp( v ) );
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
