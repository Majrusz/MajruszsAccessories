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
		holder.addModifier( ( item, configKey ) -> new MoreChestLoot( item, configKey, 1.5 ) );
		holder.addModifier( ( item, configKey ) -> new FishingLuckBonus( item, configKey, 4 ) );
		holder.addModifier( ( item, configKey ) -> new EnhanceTamedAnimal( item, configKey, 0.25 ) );
		holder.addModifier( ( item, configKey ) -> new SpawnTwins( item, configKey, 0.3 ) );
		holder.addModifier( ( item, configKey ) -> new ExtraStoneLoot( item, configKey, 0.04 ) );
		holder.addModifier( ( item, configKey ) -> new EnhancePotions( item, configKey, 0.5, 1 ) );
		holder.addModifier( ( item, configKey ) -> new DoubleCrops( item, configKey, 0.3 ) );
		holder.addModifier( ( item, configKey ) -> new ReduceDamageReceived( item, configKey, 0.0625 ) );

		return holder::getRegistry;
	}
}
