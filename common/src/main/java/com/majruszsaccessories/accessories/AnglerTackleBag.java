package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingExtraItems;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class AnglerTackleBag extends AccessoryHandler {
	public AnglerTackleBag() {
		super( MajruszsAccessories.ANGLER_TACKLE_BAG );

		this.add( FishingLuckBonus.create( 3 ) )
			.add( FishingLureBonus.create( 0.25f ) )
			.add( FishingExtraItems.create( 0.25f, 3 ) );
	}
}
