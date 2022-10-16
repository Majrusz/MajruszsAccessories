package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class WhiteFlagItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "white_flag" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "WhiteFlag", "" ) );

	public static Supplier< WhiteFlagItem > create() {
		GameModifiersHolder< WhiteFlagItem > holder = AccessoryItem.newHolder( ID, WhiteFlagItem::new );

		return holder::getRegistry;
	}
}
