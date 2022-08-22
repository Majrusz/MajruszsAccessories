package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.DoubleCrops;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class TamedPotatoBeetleItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "tamed_potato_beetle" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "TamedPotatoBeetle", "" ) );

	public static Supplier< TamedPotatoBeetleItem > create() {
		GameModifiersHolder< TamedPotatoBeetleItem > holder = new GameModifiersHolder<>( ID, TamedPotatoBeetleItem::new );
		holder.addModifier( DoubleCrops::new );

		return holder::getRegistry;
	}
}
