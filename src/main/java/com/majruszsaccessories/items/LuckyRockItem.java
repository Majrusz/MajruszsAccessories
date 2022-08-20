package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.ExtraStoneLoot;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoadContext;
import com.mlib.gamemodifiers.data.OnLootData;
import com.mlib.gamemodifiers.data.OnLootTableCustomLoadData;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class LuckyRockItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "lucky_rock" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "LuckyRock", "" ) );

	public static Supplier< LuckyRockItem > create() {
		GameModifiersHolder< LuckyRockItem > holder = new GameModifiersHolder<>( ID, LuckyRockItem::new );
		holder.addModifier( ExtraStoneLoot::new );
		holder.addModifier( AddEnderiumShards::new ); // adds Enderium Shards (from Majrusz's Progressive Difficulty) to loot table
		holder.addModifier( AddDropChance::new );

		return holder::getRegistry;
	}

	static class AddEnderiumShards extends AccessoryModifier {
		public AddEnderiumShards( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLootTableCustomLoadContext onLoad = new OnLootTableCustomLoadContext( this::addLoot );
			onLoad.addCondition( data->ExtraStoneLoot.LOOT_THE_END.equals( data.name ) )
				.addCondition( data->Integration.isProgressiveDifficultyInstalled() );

			this.addContext( onLoad );
		}

		private void addLoot( OnLootTableCustomLoadData data ) {
			data.addEntry( 0, com.majruszsdifficulty.Registries.ENDERIUM_SHARD.get(), 1, 0 );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLootContext onLoot = new OnLootContext( this::addExtraLoot );
			onLoot.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.0002, "drop_chance", "Chance for Lucky Rock to drop when mining stone." ) )
				.addCondition( data->data.blockState != null && data.blockState.getMaterial() == Material.STONE )
				.addCondition( OnLootContext.HAS_ENTITY )
				.addCondition( OnLootContext.HAS_ORIGIN );

			this.addContext( onLoot );
		}

		private void addExtraLoot( OnLootData data ) {
			data.generatedLoot.add( AccessoryHandler.construct( this.item.get() ) );
		}
	}
}
