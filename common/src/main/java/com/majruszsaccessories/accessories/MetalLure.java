package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingExtraItems;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class MetalLure extends AccessoryHandler {
	public MetalLure() {
		super( MajruszsAccessories.METAL_LURE );

		this.add( FishingExtraItems.create( 0.2f, 2 ) );
	}
}
