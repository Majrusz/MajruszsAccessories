package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.FishingExtraTreasure;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.components.TradeOffer;

@AutoInstance
public class MetalLure extends AccessoryHandler {
	public MetalLure() {
		super( MajruszsAccessories.METAL_LURE, MetalLure.class );

		this.add( FishingExtraTreasure.create( 0.05f ) )
			.add( AnglerTrophy.FishDropChance.create( 0.015f ) )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.ANGLER_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
