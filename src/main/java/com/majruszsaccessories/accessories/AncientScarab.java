package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.ExtraArchaeologyItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.StringListConfig;
import com.mlib.contexts.OnLoot;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.function.Supplier;
import java.util.stream.Stream;

@AutoInstance
public class AncientScarab extends AccessoryBase {
	public AncientScarab() {
		super( Registries.ANCIENT_SCARAB );

		this.name( "AncientScarab" )
			.add( ExtraArchaeologyItem.create() )
			.add( AddToSuspiciousBlocks.create() );
	}

	static class AddToSuspiciousBlocks extends AccessoryComponent {
		public static ISupplier create() {
			return AddToSuspiciousBlocks::new;
		}

		protected AddToSuspiciousBlocks( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.125, Range.CHANCE );
			chance.name( "spawn_chance" ).comment( "Chance for Ancient Scarab to spawn in suspicious blocks." );

			StringListConfig lootTableIds = new StringListConfig( Stream.of(
				BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY,
				BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
				BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON,
				BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE,
				BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY,
				BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY
			).map( ResourceLocation::toString ).toArray( String[]::new ) );
			lootTableIds.name( "loot_table_ids" ).comment( "Determines where Ancient Scarab can spawn." );

			OnLoot.listen( this::replaceGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) )
				.addCondition( CustomConditions.dropChance( chance, data->data.entity ) )
				.addCondition( OnLoot.is( lootTableIds ) )
				.addCondition( OnLoot.hasOrigin() )
				.insertTo( group );
		}
	}
}
