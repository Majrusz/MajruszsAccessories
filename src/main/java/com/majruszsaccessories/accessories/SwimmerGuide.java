package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class SwimmerGuide extends AccessoryBase {
	public SwimmerGuide() {
		super( Registries.SWIMMER_GUIDE );

		this.name( "SwimmerGuide" )
			.add( SwimmingSpeedBonus.create() );
	}
}
