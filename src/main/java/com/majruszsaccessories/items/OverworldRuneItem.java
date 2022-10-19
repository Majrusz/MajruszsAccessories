package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class OverworldRuneItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "overworld_rune" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "OverworldRune", "" ) );

	public static Supplier< OverworldRuneItem > create() {
		GameModifiersHolder< OverworldRuneItem > holder = AccessoryItem.newHolder( ID, OverworldRuneItem::new );

		return holder::getRegistry;
	}
}
