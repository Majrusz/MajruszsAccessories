package com.majruszsaccessories.boosters;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class GoldenHorseshoe extends BoosterHandler {
	public GoldenHorseshoe() {
		super( MajruszsAccessories.GOLDEN_HORSESHOE );

		this.add( LuckBonus.create( 2 ) );
	}
}
