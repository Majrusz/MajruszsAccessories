package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.*;
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
		holder.addModifier( MoreChestLoot::new );
		holder.addModifier( FishingLuckBonus::new );
		holder.addModifier( EnhanceTamedAnimal::new );
		holder.addModifier( SpawnTwins::new );
		holder.addModifier( ExtraStoneLoot::new );
		holder.addModifier( EnhancePotions::new );
		holder.addModifier( DoubleCrops::new );
		holder.addModifier( ReduceDamageReceived::new );
		holder.addModifier( ReduceDamageDealt::new );

		return holder::getRegistry;
	}
}
