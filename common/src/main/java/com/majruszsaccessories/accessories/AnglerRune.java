package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.FishingExtraItems;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class AnglerRune extends AccessoryHandler {
	public AnglerRune() {
		super( MajruszsAccessories.ANGLER_RUNE, AnglerRune.class );

		this.add( FishingLuckBonus.create( 3.5f ) )
			.add( FishingLureBonus.create( 0.25f ) )
			.add( FishingExtraItems.create( 0.25f, 3 ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
