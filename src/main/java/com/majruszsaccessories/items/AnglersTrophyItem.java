package com.majruszsaccessories.items;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.FishingLuckBonus;
import com.majruszsdifficulty.items.TreasureBagItem;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnItemFished;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class AnglersTrophyItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "anglers_trophy" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "AnglerTrophy", "" ) );

	public static Supplier< AnglersTrophyItem > create() {
		GameModifiersHolder< AnglersTrophyItem > holder = AccessoryItem.newHolder( ID, AnglersTrophyItem::new );
		holder.addModifier( FishingLuckBonus::new );
		holder.addModifier( AddDropChance::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			if( Integration.isProgressiveDifficultyInstalled() ) {
				OnLootTableCustomLoad.Context onLoad = new OnLootTableCustomLoad.Context( this::addToTreasureBag );
				onLoad.addCondition( data->TreasureBagItem.Fishing.LOCATION.equals( data.name ) );

				this.addContext( onLoad );
			} else {
				OnItemFished.Context onFished = new OnItemFished.Context( this::onFished );
				onFished.addCondition( new Condition.Chance<>( 0.00375, "drop_chance", "Chance to drop Angler's Trophy from fishing." ) );

				this.addContext( onFished );
			}
		}

		private void addToTreasureBag( OnLootTableCustomLoad.Data data ) {
			int poolId = data.addPool();
			data.addEntry( poolId, this.item.get(), 1, 4, LootItemRandomChanceCondition.randomChance( 0.075f ) );
		}

		private void onFished( OnItemFished.Data data ) {
			this.spawnFlyingItem( data.level, data.hook.position(), data.player.position() );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.FISHERMAN, 5 );
		}
	}
}
