package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.*;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class NatureEssenceItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "nature_essence" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "NatureEssence", "" ) );

	public static Supplier< NatureEssenceItem > create() {
		GameModifiersHolder< NatureEssenceItem > holder = new GameModifiersHolder<>( ID, NatureEssenceItem::new );
		holder.addModifier( EnhancePotions::new );
		holder.addModifier( EnhanceTamedAnimal::new );
		holder.addModifier( ExtraStoneLoot::new );
		holder.addModifier( FishingLuckBonus::new );
		holder.addModifier( SpawnTwins::new );

		return holder::getRegistry;
	}
}
