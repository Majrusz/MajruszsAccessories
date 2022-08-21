package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.PilgrimsFavor;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class PilgrimsBookItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "pilgrims_book" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "PilgrimsBook", "" ) );

	public static Supplier< PilgrimsBookItem > create() {
		GameModifiersHolder< PilgrimsBookItem > holder = new GameModifiersHolder<>( ID, PilgrimsBookItem::new );
		holder.addModifier( PilgrimsFavor::new );

		return holder::getRegistry;
	}
}
