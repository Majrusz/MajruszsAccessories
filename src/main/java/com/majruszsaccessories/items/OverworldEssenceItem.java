package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class OverworldEssenceItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "overworld_essence" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "OverworldEssence", "" ) );

	public static Supplier< OverworldEssenceItem > create() {
		GameModifiersHolder< OverworldEssenceItem > holder = AccessoryItem.newHolder( ID, OverworldEssenceItem::new );

		return holder::getRegistry;
	}
}
