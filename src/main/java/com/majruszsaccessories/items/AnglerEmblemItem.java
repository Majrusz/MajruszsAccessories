package com.majruszsaccessories.items;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.FishingLuckBonus;
import com.majruszsdifficulty.items.TreasureBagItem;
import com.mlib.MajruszLibrary;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoadContext;
import com.mlib.gamemodifiers.contexts.OnLootTableLoadContext;
import com.mlib.gamemodifiers.data.OnLootTableCustomLoadData;
import com.mlib.gamemodifiers.data.OnLootTableLoadData;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class AnglerEmblemItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "angler_emblem" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "AnglerEmblem", "" ) );

	public static Supplier< AnglerEmblemItem > create() {
		GameModifiersHolder< AnglerEmblemItem > holder = new GameModifiersHolder<>( ID, AnglerEmblemItem::new );
		holder.addModifier( FishingLuckBonus::new );
		holder.addModifier( AddToTreasureBag::new );

		return holder::getRegistry;
	}

	static class AddToTreasureBag extends GameModifier {
		final Supplier< ? extends AccessoryItem > item;

		public AddToTreasureBag( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( configKey, "", "" );
			this.item = item;

			OnLootTableCustomLoadContext onLoad = new OnLootTableCustomLoadContext( this::addLoot );
			onLoad.addCondition( data->Integration.isProgressiveDifficultyInstalled() )
				.addCondition( data->TreasureBagItem.Fishing.LOCATION.equals( data.name ) );

			this.addContext( onLoad );
		}

		private void addLoot( OnLootTableCustomLoadData data ) {
			int poolId = data.addPool();
			data.addEntry( poolId, this.item.get(), 1, 4, LootItemRandomChanceCondition.randomChance( 0.075f ) );
		}
	}
}
