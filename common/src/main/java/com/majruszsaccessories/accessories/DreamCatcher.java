package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.SleepingBonuses;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class DreamCatcher extends AccessoryHandler {
	public DreamCatcher() {
		super( MajruszsAccessories.DREAM_CATCHER );

		this.add( SleepingBonuses.create( 1, 300 ) );
	}
}
