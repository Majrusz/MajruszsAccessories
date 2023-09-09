package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.majruszsaccessories.accessories.components.SaturationBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class ToughRock extends AccessoryBase {
	public ToughRock() {
		super( Registries.TOUGH_ROCK );

		this.name( "ToughRock" )
			.add( MiningSpeedBonus.create() );
	}
}
