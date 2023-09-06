package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class PearlBarleyBucket extends AccessoryBase {
	public PearlBarleyBucket() {
		super( Registries.PEARL_BARLEY_BUCKET );

		this.name( "PearlBarleyBucket" )
			.add( FishingLureBonus.create() );
	}
}
