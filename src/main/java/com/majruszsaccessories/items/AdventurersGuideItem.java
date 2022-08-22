package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.MoreChestLoot;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class AdventurersGuideItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "adventurers_guide" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "AdventurersGuide", "" ) );

	public static Supplier< AdventurersGuideItem > create() {
		GameModifiersHolder< AdventurersGuideItem > holder = new GameModifiersHolder<>( ID, AdventurersGuideItem::new );
		holder.addModifier( MoreChestLoot::new );

		return holder::getRegistry;
	}
}
