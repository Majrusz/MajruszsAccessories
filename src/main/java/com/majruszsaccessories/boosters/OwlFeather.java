package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.ExperienceBonus;
import com.mlib.annotations.AutoInstance;

@AutoInstance
public class OwlFeather extends BoosterBase {
	public OwlFeather() {
		super( Registries.OWL_FEATHER );

		this.name( "OwlFeather" )
			.add( ExperienceBonus.create( 0.15 ) );
	}
}
