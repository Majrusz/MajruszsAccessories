package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.DoubleCrops;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class TamedPotatoBeetleItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "tamed_potato_beetle" );

	public TamedPotatoBeetleItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "TamedPotatoBeetle" );

			new DoubleCrops( Registries.TAMED_POTATO_BEETLE, ID );
			new AddDropChance( Registries.TAMED_POTATO_BEETLE, ID );
			new TradeOffer( Registries.TAMED_POTATO_BEETLE, ID );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new DoubleCrops.OnCropsContext( this::addToGeneratedLoot )
				.addCondition( new DropChance( 0.002 ) )
				.insertTo( this );
		}

		static class DropChance extends Condition.Chance< OnLoot.Data > {
			public DropChance( double chance ) {
				super( chance );

				this.chance.name( "drop_chance" ).comment( "Chance for Tamed Potato Beetle to drop when harvesting." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.FARMER, 5 );
		}
	}
}
