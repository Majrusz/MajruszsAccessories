package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.BrushingExtraItem;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class AdventurerRune extends AccessoryHandler {
	public AdventurerRune() {
		super( MajruszsAccessories.ADVENTURER_RUNE );

		this.add( MoreChestLoot.create( 1.5f ) )
			.add( BrushingExtraItem.create( 0.2f ) )
			.add( SwimmingSpeedBonus.create( 0.25f ) )
			.add( TradeOffer.create( 17 ) );
	}
}
