package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class SwimmerGuide extends AccessoryHandler {
	public SwimmerGuide() {
		super( MajruszsAccessories.SWIMMER_GUIDE );

		this.add( SwimmingSpeedBonus.create( 0.2f ) );
	}
}
