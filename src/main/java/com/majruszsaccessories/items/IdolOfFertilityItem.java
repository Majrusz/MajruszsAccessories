package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.SpawnTwins;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnBabySpawn;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class IdolOfFertilityItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "idol_of_fertility" );

	public IdolOfFertilityItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "IdolOfFertility" );

			new SpawnTwins( Registries.IDOL_OF_FERTILITY, ID );
			new AddDropChance( Registries.IDOL_OF_FERTILITY, ID );
			new TradeOffer( Registries.IDOL_OF_FERTILITY, ID );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new SpawnTwins.OnTwinsSpawnContext( this::spawnTotem )
				.addCondition( new DropChance( 0.005 ) )
				.insertTo( this );
		}

		private void spawnTotem( OnBabySpawn.Data data ) {
			this.spawnFlyingItem( data.level, data.parentA.position(), data.parentB.position() );
		}

		static class DropChance extends Condition.Chance< OnBabySpawn.Data > {
			public DropChance( double chance ) {
				super( chance );

				this.chance.name( "drop_chance" ).comment( "Chance for Idol of Fertility to drop when breeding animals." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.BUTCHER, 5 );
		}
	}
}
