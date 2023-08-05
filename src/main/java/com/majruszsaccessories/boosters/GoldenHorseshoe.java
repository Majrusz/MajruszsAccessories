package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class GoldenHorseshoe extends BoosterBase {
	public GoldenHorseshoe() {
		super( Registries.GOLDEN_HORSESHOE );

		this.name( "GoldenHorseshoe" )
			.add( LuckBonus.create( 2 ) );
	}
}
