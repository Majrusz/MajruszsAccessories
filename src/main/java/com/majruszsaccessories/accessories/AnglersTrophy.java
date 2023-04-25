package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.AccessoryComponent;
import com.majruszsaccessories.components.FishingLuckBonus;
import com.majruszsaccessories.components.TradeOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsdifficulty.items.TreasureBagItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnItemFished;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.Supplier;

@AutoInstance
public class AnglersTrophy extends AccessoryBase {
	public AnglersTrophy() {
		super( Registries.ANGLERS_TROPHY );

		this.name( "AnglersTrophy" )
			.add( FishingLuckBonus.create() )
			.add( TradeOffer.create( VillagerProfession.FISHERMAN, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static AccessoryComponent.ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			if( Integration.isProgressiveDifficultyInstalled() ) {
				OnLootTableCustomLoad.listen( this::addToTreasureBag )
					.addCondition( Condition.predicate( data->TreasureBagItem.Fishing.LOCATION.equals( data.name ) ) )
					.insertTo( group );
			} else {
				DoubleConfig chance = new DoubleConfig( 0.015, Range.CHANCE );
				chance.name( "drop_chance" ).comment( "Chance to drop Angler's Trophy when fishing." );

				OnItemFished.listen( this::onFished )
					.addCondition( Condition.chance( chance ) )
					.insertTo( group );
			}
		}

		private void addToTreasureBag( OnLootTableCustomLoad.Data data ) {
			data.addEntry( data.addPool(), this.item.get(), 1, 4, LootItemRandomChanceCondition.randomChance( 0.30f ) );
		}

		private void onFished( OnItemFished.Data data ) {
			this.spawnFlyingItem( data.getLevel(), data.hook.position(), data.player.position() );
		}
	}
}
