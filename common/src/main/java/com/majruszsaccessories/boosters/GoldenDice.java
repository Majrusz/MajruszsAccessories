package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.majruszsaccessories.boosters.components.BoosterIncompatibility;
import com.majruszsaccessories.common.BoosterHandler;

@AutoInstance
public class GoldenDice extends BoosterHandler {
	public GoldenDice() {
		super( MajruszsAccessories.GOLDEN_DICE, GoldenDice.class );

		this.add( AccessoryDropChance.create( 0.4f ) )
			.add( BoosterIncompatibility.create( MajruszsAccessories.DICE ) );
	}
}
