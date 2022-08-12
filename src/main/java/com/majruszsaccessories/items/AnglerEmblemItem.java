package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.FishingLuckBonus;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class AnglerEmblemItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "angler_emblem" );

	static {
		CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "AnglerEmblem", "" ) );
	}

	public static Supplier< AnglerEmblemItem > create() {
		GameModifiersHolder< AnglerEmblemItem > holder = new GameModifiersHolder<>( ID, AnglerEmblemItem::new );
		holder.addModifier( FishingLuckBonus::new );

		return holder::getRegistry;
	}
}
