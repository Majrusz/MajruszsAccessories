package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.level.BlockHelper;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

@AutoInstance
public class SwimmerGuide extends AccessoryHandler {
	public SwimmerGuide() {
		super( MajruszsAccessories.SWIMMER_GUIDE, SwimmerGuide.class );

		this.add( SwimmingSpeedBonus.create( 0.2f ) )
			.add( UnderwaterChestDropChance.create() )
			.add( BuriedTreasureDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.ADVENTURER_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class UnderwaterChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.05f;

		public static ISupplier< AccessoryItem > create() {
			return UnderwaterChestDropChance::new;
		}

		protected UnderwaterChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.hasLevel() )
				.addCondition( data->data.origin != null )
				.addCondition( data->BlockHelper.getState( data.getLevel(), data.origin ).getFluidState().isSourceOfType( Fluids.WATER ) )
				.addCondition( data->data.lootId.toString().contains( "chest" ) )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.entity ) );

			handler.getConfig()
				.define( "underwater_chest_spawn_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}

	static class BuriedTreasureDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.25f;

		public static ISupplier< AccessoryItem > create() {
			return BuriedTreasureDropChance::new;
		}

		protected BuriedTreasureDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.hasLevel() )
				.addCondition( data->data.origin != null )
				.addCondition( data->data.lootId.equals( BuiltInLootTables.BURIED_TREASURE ) )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.entity ) );

			handler.getConfig()
				.define( "buried_treasure_spawn_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
