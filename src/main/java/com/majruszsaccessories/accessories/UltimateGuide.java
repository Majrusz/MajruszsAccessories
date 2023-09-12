package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class UltimateGuide extends AccessoryBase {
	public UltimateGuide() {
		super( Registries.ULTIMATE_GUIDE );

		this.name( "UltimateGuide" )
			.add( MoreChestLoot.create( 1.5 ) )
			.add( MiningSpeedBonus.create( 0.15 ) )
			.add( SwimmingSpeedBonus.create( 0.3 ) );
	}
}
