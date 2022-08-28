package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.DoubleCrops;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLoot;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class TamedPotatoBeetleItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "tamed_potato_beetle" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "TamedPotatoBeetle", "" ) );

	public static Supplier< TamedPotatoBeetleItem > create() {
		GameModifiersHolder< TamedPotatoBeetleItem > holder = AccessoryItem.newHolder( ID, TamedPotatoBeetleItem::new );
		holder.addModifier( DoubleCrops::new );
		holder.addModifier( AddDropChance::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLoot.Context onLoot = DoubleCrops.lootContext( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.Chance( 0.002, "drop_chance", "Chance for Tamed Potato Beetle to drop from crops." ) );

			this.addContext( onLoot );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.FARMER, 5 );
		}
	}
}
