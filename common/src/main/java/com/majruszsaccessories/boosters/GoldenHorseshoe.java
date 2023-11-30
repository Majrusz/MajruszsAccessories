package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.BoosterIncompatibility;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.majruszsaccessories.common.BoosterHandler;

@AutoInstance
public class GoldenHorseshoe extends BoosterHandler {
	public GoldenHorseshoe() {
		super( MajruszsAccessories.GOLDEN_HORSESHOE, GoldenHorseshoe.class );

		this.add( LuckBonus.create( 2 ) )
			.add( BoosterIncompatibility.create( MajruszsAccessories.HORSESHOE ) );
	}
}
