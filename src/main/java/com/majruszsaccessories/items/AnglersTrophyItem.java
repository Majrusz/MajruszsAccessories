package com.majruszsaccessories.items;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.FishingLuckBonus;
import com.majruszsdifficulty.items.TreasureBagItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnItemFished;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class AnglersTrophyItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "anglers_trophy" );

	public AnglersTrophyItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "AnglerTrophy" );

			new FishingLuckBonus( Registries.ANGLERS_TROPHY, ID );
			new AddDropChance( Registries.ANGLERS_TROPHY, ID );
			new TradeOffer( Registries.ANGLERS_TROPHY, ID );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			if( Integration.isProgressiveDifficultyInstalled() ) {
				new OnLootTableCustomLoad.Context( this::addToTreasureBag )
					.addCondition( data->TreasureBagItem.Fishing.LOCATION.equals( data.name ) )
					.insertTo( this );
			} else {
				new OnItemFished.Context( this::onFished )
					.addCondition( new DropChance( 0.00375 ) )
					.insertTo( this );
			}
		}

		private void addToTreasureBag( OnLootTableCustomLoad.Data data ) {
			int poolId = data.addPool();
			data.addEntry( poolId, this.item.get(), 1, 4, LootItemRandomChanceCondition.randomChance( 0.075f ) );
		}

		private void onFished( OnItemFished.Data data ) {
			this.spawnFlyingItem( data.level, data.hook.position(), data.player.position() );
		}

		static class DropChance extends Condition.Chance< OnItemFished.Data > {
			public DropChance( double chance ) {
				super( chance );

				this.chance.name( "drop_chance" ).comment( "Chance to drop Angler's Trophy when fishing." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.FISHERMAN, 5 );
		}
	}
}
