package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.*;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class NaturesEssenceItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "natures_essence" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "NaturesEssence", "" ) );

	public static Supplier< NaturesEssenceItem > create() {
		GameModifiersHolder< NaturesEssenceItem > holder = new GameModifiersHolder<>( ID, NaturesEssenceItem::new );
		holder.addModifier( EnhancePotions::new );
		holder.addModifier( EnhanceTamedAnimal::new );
		holder.addModifier( ExtraStoneLoot::new );
		holder.addModifier( FishingLuckBonus::new );
		holder.addModifier( SpawnTwins::new );

		return holder::getRegistry;
	}
}
