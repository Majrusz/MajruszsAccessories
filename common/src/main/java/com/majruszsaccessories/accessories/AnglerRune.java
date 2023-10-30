package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingExtraItems;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class AnglerRune extends AccessoryHandler {
	public AnglerRune() {
		super( MajruszsAccessories.ANGLER_RUNE );

		this.add( FishingLuckBonus.create( 3 ) )
			.add( FishingLureBonus.create( 0.25f ) )
			.add( FishingExtraItems.create( 0.25f, 3 ) )
			.add( TradeOffer.create( 17 ) );
	}
}
