package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.ExtraStoneLoot;
import com.majruszsaccessories.gamemodifiers.list.FishingLuckBonus;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class LuckyRockItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "lucky_rock" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "LuckyRock", "" ) );

	public static Supplier< LuckyRockItem > create() {
		GameModifiersHolder< LuckyRockItem > holder = new GameModifiersHolder<>( ID, LuckyRockItem::new );
		holder.addModifier( ExtraStoneLoot::new );

		return holder::getRegistry;
	}
}
