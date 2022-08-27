package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.MoreChestLoot;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class AdventurersGuideItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "adventurers_guide" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "AdventurersGuide", "" ) );

	public static Supplier< AdventurersGuideItem > create() {
		GameModifiersHolder< AdventurersGuideItem > holder = AccessoryItem.newHolder( ID, AdventurersGuideItem::new );
		holder.addModifier( MoreChestLoot::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.CARTOGRAPHER, 5 );
		}
	}
}
