package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.mlib.annotations.AutoInstance;

@AutoInstance
public class GoldenDice extends BoosterBase {
	public GoldenDice() {
		super( Registries.GOLDEN_DICE );

		this.name( "GoldenDice" )
			.add( AccessoryDropChance.create( 0.4 ) );
	}
}
