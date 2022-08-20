package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.FishingLuckBonus;
import com.majruszsdifficulty.items.TreasureBagItem;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnItemFishedContext;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoadContext;
import com.mlib.gamemodifiers.data.OnItemFishedData;
import com.mlib.gamemodifiers.data.OnLootTableCustomLoadData;
import com.mlib.levels.LevelHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class AnglerEmblemItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "angler_emblem" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "AnglerEmblem", "" ) );

	public static Supplier< AnglerEmblemItem > create() {
		GameModifiersHolder< AnglerEmblemItem > holder = new GameModifiersHolder<>( ID, AnglerEmblemItem::new );
		holder.addModifier( FishingLuckBonus::new );
		holder.addModifier( AddDropChance::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			if( Integration.isProgressiveDifficultyInstalled() ) {
				OnLootTableCustomLoadContext onLoad = new OnLootTableCustomLoadContext( this::addToTreasureBag );
				onLoad.addCondition( data->TreasureBagItem.Fishing.LOCATION.equals( data.name ) );

				this.addContext( onLoad );
			} else {
				OnItemFishedContext onFished = new OnItemFishedContext( this::onFished );
				onFished.addCondition( new Condition.Chance( 0.00375, "drop_chance", "Chance to drop Angler Emblem from fishing." ) );

				this.addContext( onFished );
			}
		}

		private void addToTreasureBag( OnLootTableCustomLoadData data ) {
			int poolId = data.addPool();
			data.addEntry( poolId, this.item.get(), 1, 4, LootItemRandomChanceCondition.randomChance( 0.075f ) );
		}

		private void onFished( OnItemFishedData data ) {
			assert data.level != null;
			ItemStack anglerEmblem = AccessoryHandler.construct( this.item.get() );
			LevelHelper.spawnItemEntityFlyingTowardsDirection( anglerEmblem, data.level, data.hook.position(), data.player.position() );
		}
	}
}
