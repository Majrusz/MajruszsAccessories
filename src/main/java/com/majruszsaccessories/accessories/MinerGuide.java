package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class MinerGuide extends AccessoryBase {
	public MinerGuide() {
		super( Registries.MINER_GUIDE );

		this.name( "MinerGuide" )
			.add( MiningSpeedBonus.create() );
	}
}
