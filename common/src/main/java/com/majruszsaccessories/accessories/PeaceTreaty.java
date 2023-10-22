package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.LowerSpawnRate;
import com.majruszsaccessories.accessories.components.ReduceDamage;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class PeaceTreaty extends AccessoryHandler {
	public PeaceTreaty() {
		super( MajruszsAccessories.PEACE_TREATY );

		this.add( LowerSpawnRate.create( 0.12f ) )
			.add( ReduceDamage.create( 0.24f ) );
	}
}
