package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningDurabilityBonus;
import com.majruszsaccessories.accessories.components.MiningExtraItem;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class MinerEmblem extends AccessoryHandler {
	public MinerEmblem() {
		super( MajruszsAccessories.MINER_EMBLEM );

		this.add( MiningExtraItem.create( 0.04f ) )
			.add( MiningSpeedBonus.create( 0.12f ) )
			.add( MiningDurabilityBonus.create( 0.12f ) );
	}
}
