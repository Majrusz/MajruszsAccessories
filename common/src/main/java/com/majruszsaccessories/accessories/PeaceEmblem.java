package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.LowerSpawnRate;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class PeaceEmblem extends AccessoryHandler {
	public PeaceEmblem() {
		super( MajruszsAccessories.PEACE_EMBLEM );

		this.add( LowerSpawnRate.create( 0.1f ) );
	}
}
