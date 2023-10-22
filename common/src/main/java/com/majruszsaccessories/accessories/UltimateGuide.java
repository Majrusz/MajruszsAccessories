package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class UltimateGuide extends AccessoryHandler {
	public UltimateGuide() {
		super( MajruszsAccessories.ULTIMATE_GUIDE );

		this.add( MoreChestLoot.create( 1.5f ) )
			.add( MiningSpeedBonus.create( 0.15f ) )
			.add( SwimmingSpeedBonus.create( 0.3f ) );
	}
}
