package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class MinerGuide extends AccessoryHandler {
	public MinerGuide() {
		super( MajruszsAccessories.MINER_GUIDE );

		this.add( MiningSpeedBonus.create( 0.1f ) );
	}
}
