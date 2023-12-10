package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.BrushingExtraItem;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;

@AutoInstance
public class AncientScarab extends AccessoryHandler {
	public AncientScarab() {
		super( MajruszsAccessories.ANCIENT_SCARAB, AncientScarab.class );

		this.add( BrushingExtraItem.create( 0.16f ) )
			.add( SuspiciousBlocksDropChance.create() )
			.add( TradeOffer.create() );
	}

	static class SuspiciousBlocksDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.125f;
		List< ResourceLocation > locations = List.of(
			BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY,
			BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
			BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON,
			BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE,
			BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY,
			BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY
		);

		public static ISupplier< AccessoryItem > create() {
			return SuspiciousBlocksDropChance::new;
		}

		protected SuspiciousBlocksDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::replaceGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) )
				.addCondition( data->this.locations.contains( data.lootId ) )
				.addCondition( data->data.origin != null );

			handler.getConfig()
				.define( "suspicious_block_spawn_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) )
				.define( "suspicious_block_ids", Reader.list( Reader.location() ), s->this.locations, ( s, v )->this.locations = v );
		}
	}
}
