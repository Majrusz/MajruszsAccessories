package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class AdventurerRune extends AccessoryHandler {
	public AdventurerRune() {
		super( MajruszsAccessories.ADVENTURER_RUNE, AdventurerRune.class );

		this.add( MoreChestLoot.create( 1.5f ) )
			.add( SwimmingSpeedBonus.create( 0.25f ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
