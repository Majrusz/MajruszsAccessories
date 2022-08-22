package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;

import java.util.function.Supplier;

public class DoubleCrops extends AccessoryModifier {
	final AccessoryPercent chance = new AccessoryPercent( "double_crops_chance", "Chance to double crops when harvesting.", false, 0.35, 0.0, 10.0 );

	public DoubleCrops( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		this.addConfig( this.chance );
		this.addTooltip( this.chance, "majruszsaccessories.bonuses.double_crops" );
	}
}
