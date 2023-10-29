package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.BrushingExtraItem;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;

@AutoInstance
public class AncientScarab extends AccessoryHandler {
	public AncientScarab() {
		super( MajruszsAccessories.ANCIENT_SCARAB );

		this.add( BrushingExtraItem.create( 0.16f ) )
			.add( SuspiciousBlocksDropChance.create() )
			.add( TradeOffer.create( 7 ) );
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

			Serializable config = handler.getConfig();
			config.defineFloat( "suspicious_block_spawn_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
			config.defineLocation( "suspicious_block_ids", ()->this.locations, x->this.locations = x );
		}
	}
}
