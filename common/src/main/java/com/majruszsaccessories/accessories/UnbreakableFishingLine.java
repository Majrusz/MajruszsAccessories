package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.components.TradeOffer;

@AutoInstance
public class UnbreakableFishingLine extends AccessoryHandler {
	public UnbreakableFishingLine() {
		super( MajruszsAccessories.UNBREAKABLE_FISHING_LINE, UnbreakableFishingLine.class );

		this.add( FishingLureBonus.create( 0.2f ) )
			.add( AnglerTrophy.FishingDropChance.create( 0.005f ) )
			.add( AnglerTrophy.FishDropChance.create( 0.005f ) )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.ANGLER_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
