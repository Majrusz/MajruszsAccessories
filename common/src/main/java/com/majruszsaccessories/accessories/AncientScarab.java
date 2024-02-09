package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszlibrary.text.RegexString;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.BrushingExtraItem;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;

@AutoInstance
public class AncientScarab extends AccessoryHandler {
	public AncientScarab() {
		super( MajruszsAccessories.ANCIENT_SCARAB, AncientScarab.class );

		this.add( BrushingExtraItem.create( 0.16f ) )
			.add( SuspiciousBlocksDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.ADVENTURER_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class SuspiciousBlocksDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.125f;
		List< RegexString > locations = RegexString.toRegex( List.of(
			BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY.toString(),
			BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY.toString(),
			BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.toString(),
			BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.toString(),
			BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.toString(),
			BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.toString(),
			"{regex}.*archaeology.*"
		) );

		public static ISupplier< AccessoryItem > create() {
			return SuspiciousBlocksDropChance::new;
		}

		protected SuspiciousBlocksDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::replaceGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) )
				.addCondition( data->this.locations.stream().anyMatch( id->id.matches( data.lootId.toString() ) ) )
				.addCondition( data->data.origin != null );

			handler.getConfig()
				.define( "suspicious_block_spawn_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) )
				.define( "suspicious_block_ids", Reader.list( Reader.string() ), s->RegexString.toString( this.locations ), ( s, v )->this.locations = RegexString.toRegex( v ) );
		}
	}
}
