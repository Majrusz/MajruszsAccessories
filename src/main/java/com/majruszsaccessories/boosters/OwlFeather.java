package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.ExtraExperience;
import com.mlib.annotations.AutoInstance;

@AutoInstance
public class OwlFeather extends BoosterBase {
	public OwlFeather() {
		super( Registries.OWL_FEATHER );

		this.name( "OwlFeather" )
			.add( ExtraExperience.create( 0.15 ) );
	}
}
