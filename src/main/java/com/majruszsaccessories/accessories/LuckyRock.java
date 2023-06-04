package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.ExtraStoneLoot;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class LuckyRock extends AccessoryBase {
	public LuckyRock() {
		super( Registries.LUCKY_ROCK );

		this.name( "LuckyRock" )
			.add( ExtraStoneLoot.create() )
			.add( EnderiumShardsCompatibility.create() ) // adds Enderium Shards (from Majrusz's Progressive Difficulty) to loot table
			.add( TradeOffer.create( VillagerProfession.MASON, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.0002, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Lucky Rock to drop when mining stone." );

			ExtraStoneLoot.OnStoneMined.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( chance, data->data.entity ) )
				.insertTo( group );
		}
	}

	static class EnderiumShardsCompatibility extends AccessoryComponent {
		public static ISupplier create() {
			return EnderiumShardsCompatibility::new;
		}

		protected EnderiumShardsCompatibility( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			OnLootTableCustomLoad.listen( this::addLoot )
				.addCondition( Condition.predicate( data->ExtraStoneLoot.LOOT_THE_END.equals( data.name ) ) )
				.addCondition( Condition.predicate( Integration::isProgressiveDifficultyInstalled ) )
				.insertTo( group );
		}

		private void addLoot( OnLootTableCustomLoad.Data data ) {
			data.addEntry( 0, com.majruszsdifficulty.Registries.ENDERIUM_SHARD.get(), 1, 0 );
		}
	}
}
