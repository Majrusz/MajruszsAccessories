package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.mlib.annotations.AutoInstance;

@AutoInstance
public class Horseshoe extends BoosterBase {
	public Horseshoe() {
		super( Registries.HORSESHOE );

		this.name( "Horseshoe" )
			.add( LuckBonus.create( 1 ) );
	}
}
