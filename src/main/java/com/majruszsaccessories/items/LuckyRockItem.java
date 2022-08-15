package com.majruszsaccessories.items;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.ExtraStoneLoot;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoadContext;
import com.mlib.gamemodifiers.data.OnLootTableCustomLoadData;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class LuckyRockItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "lucky_rock" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "LuckyRock", "" ) );

	public static Supplier< LuckyRockItem > create() {
		GameModifiersHolder< LuckyRockItem > holder = new GameModifiersHolder<>( ID, LuckyRockItem::new );
		holder.addModifier( ExtraStoneLoot::new );
		holder.addModifier( AddEnderiumShards::new ); // adds Enderium Shards (from Majrusz's Progressive Difficulty) to loot table

		return holder::getRegistry;
	}

	static class AddEnderiumShards extends GameModifier {
		public AddEnderiumShards( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( configKey, "", "" );

			OnLootTableCustomLoadContext onLoad = new OnLootTableCustomLoadContext( this::addLoot );
			onLoad.addCondition( data->ExtraStoneLoot.LOOT_THE_END.equals( data.name ) )
				.addCondition( data->Integration.isProgressiveDifficultyInstalled() );

			this.addContext( onLoad );
		}

		private void addLoot( OnLootTableCustomLoadData data ) {
			data.addEntry( 0, com.majruszsdifficulty.Registries.ENDERIUM_SHARD.get(), 1, 0 );
		}
	}
}
