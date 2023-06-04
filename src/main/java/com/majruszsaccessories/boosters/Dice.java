package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.mlib.annotations.AutoInstance;

@AutoInstance
public class Dice extends BoosterBase {
	public Dice() {
		super( Registries.DICE );

		this.name( "Dice" )
			.add( AccessoryDropChance.create( 0.2 ) );
	}
}
