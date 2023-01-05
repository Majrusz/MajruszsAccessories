package com.majruszsaccessories.items;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.ExtraStoneLoot;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.contexts.OnLootTableCustomLoad;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class LuckyRockItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "lucky_rock" );

	public LuckyRockItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "LuckyRock" );

			new ExtraStoneLoot( Registries.LUCKY_ROCK, ID );
			new AddEnderiumShards( Registries.LUCKY_ROCK, ID ); // adds Enderium Shards (from Majrusz's Progressive Difficulty) to loot table
			new AddDropChance( Registries.LUCKY_ROCK, ID );
			new TradeOffer( Registries.LUCKY_ROCK, ID );
		}
	}

	static class AddEnderiumShards extends AccessoryModifier {
		public AddEnderiumShards( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new OnLootTableCustomLoad.Context( this::addLoot )
				.addCondition( data->ExtraStoneLoot.LOOT_THE_END.equals( data.name ) )
				.addCondition( data->Integration.isProgressiveDifficultyInstalled() )
				.insertTo( this );
		}

		private void addLoot( OnLootTableCustomLoad.Data data ) {
			data.addEntry( 0, com.majruszsdifficulty.Registries.ENDERIUM_SHARD.get(), 1, 0 );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new ExtraStoneLoot.OnStoneMinedContext( this::addToGeneratedLoot )
				.addCondition( new DropChance( 0.0002 ) )
				.insertTo( this );
		}

		static class DropChance extends Condition.Chance< OnLoot.Data > {
			public DropChance( double chance ) {
				super( chance );

				this.chance.name( "drop_chance" ).comment( "Chance for Lucky Rock to drop when mining stone." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.MASON, 5 );
		}
	}
}
