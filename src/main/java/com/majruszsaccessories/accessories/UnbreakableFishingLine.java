package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class UnbreakableFishingLine extends AccessoryBase {
	public UnbreakableFishingLine() {
		super( Registries.UNBREAKABLE_FISHING_LINE );

		this.name( "UnbreakableFishingLine" )
			.add( FishingLureBonus.create() );
	}
}
