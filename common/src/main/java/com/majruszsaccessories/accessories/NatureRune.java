package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.BreedingTwins;
import com.majruszsaccessories.accessories.components.HarvestingDoubleCrops;
import com.majruszsaccessories.accessories.components.TamingStrongerAnimals;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class NatureRune extends AccessoryHandler {
	public NatureRune() {
		super( MajruszsAccessories.NATURE_RUNE );

		this.add( TamingStrongerAnimals.create( 0.24f ) )
			.add( BreedingTwins.create( 0.3f ) )
			.add( HarvestingDoubleCrops.create( 0.3f ) );
	}
}
