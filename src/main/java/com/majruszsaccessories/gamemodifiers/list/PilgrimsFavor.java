package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;

import java.util.function.Supplier;

public class PilgrimsFavor extends AccessoryModifier {
	final AccessoryPercent sizeMultiplier = new AccessoryPercent( "chest_size_bonus", "Extra multiplier for number of items acquired from chests.", false, 0.5, 0.0, 10.0 );

	public PilgrimsFavor( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		this.addConfig( this.sizeMultiplier );
		this.addTooltip( "majruszsaccessories.bonuses.starvation_immunity" );
		this.addTooltip( this.sizeMultiplier, "majruszsaccessories.bonuses.better_chest_loot" );
	}
}
