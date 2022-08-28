package com.majruszsaccessories.items;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.ExtraStoneLoot;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class LuckyRockItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "lucky_rock" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "LuckyRock", "" ) );

	public static Supplier< LuckyRockItem > create() {
		GameModifiersHolder< LuckyRockItem > holder = AccessoryItem.newHolder( ID, LuckyRockItem::new );
		holder.addModifier( ExtraStoneLoot::new );
		holder.addModifier( AddEnderiumShards::new ); // adds Enderium Shards (from Majrusz's Progressive Difficulty) to loot table
		holder.addModifier( AddDropChance::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class AddEnderiumShards extends AccessoryModifier {
		public AddEnderiumShards( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLootTableCustomLoad.Context onLoad = new OnLootTableCustomLoad.Context( this::addLoot );
			onLoad.addCondition( data->ExtraStoneLoot.LOOT_THE_END.equals( data.name ) )
				.addCondition( data->Integration.isProgressiveDifficultyInstalled() );

			this.addContext( onLoad );
		}

		private void addLoot( OnLootTableCustomLoad.Data data ) {
			data.addEntry( 0, com.majruszsdifficulty.Registries.ENDERIUM_SHARD.get(), 1, 0 );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLoot.Context onLoot = ExtraStoneLoot.lootContext( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.Chance( 0.0002, "drop_chance", "Chance for Lucky Rock to drop when mining stone." ) );

			this.addContext( onLoot );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.MASON, 5 );
		}
	}
}
