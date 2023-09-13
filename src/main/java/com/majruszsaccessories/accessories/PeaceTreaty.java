package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.LowerSpawnRate;
import com.majruszsaccessories.accessories.components.ReduceDamage;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class PeaceTreaty extends AccessoryBase {
	public PeaceTreaty() {
		super( Registries.PEACE_TREATY );

		this.name( "PeaceTreaty" )
			.add( LowerSpawnRate.create( 0.12 ) )
			.add( ReduceDamage.create( 0.24 ) );
	}
}
