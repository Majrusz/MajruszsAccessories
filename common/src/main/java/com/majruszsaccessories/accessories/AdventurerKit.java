package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.BrushingExtraItem;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class AdventurerKit extends AccessoryHandler {
	public AdventurerKit() {
		super( MajruszsAccessories.ADVENTURER_KIT );

		this.add( MoreChestLoot.create( 1.5f ) )
			.add( BrushingExtraItem.create( 0.2f ) )
			.add( SwimmingSpeedBonus.create( 0.25f ) );
	}
}
