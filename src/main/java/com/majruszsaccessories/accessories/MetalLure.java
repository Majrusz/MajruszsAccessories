package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.ExtraFishingItems;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class MetalLure extends AccessoryBase {
	public MetalLure() {
		super( Registries.METAL_LURE );

		this.name( "MetalLure" )
			.add( ExtraFishingItems.create() );
	}
}
