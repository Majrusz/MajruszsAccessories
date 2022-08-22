package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;

import java.util.function.Supplier;

public class MoreChestLoot extends AccessoryModifier {
	final AccessoryPercent sizeMultiplier = new AccessoryPercent( "chest_size_bonus", "Extra multiplier for number of items acquired from chests.", false, 0.5, 0.0, 10.0 );

	public MoreChestLoot( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		this.addConfig( this.sizeMultiplier );
		this.addTooltip( this.sizeMultiplier, "majruszsaccessories.bonuses.more_chest_loot" );
	}
}
