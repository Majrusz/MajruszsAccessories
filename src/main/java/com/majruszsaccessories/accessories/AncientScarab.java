package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.ExtraArchaeologyItem;
import com.mlib.modhelper.AutoInstance;

@AutoInstance
public class AncientScarab extends AccessoryBase {
	public AncientScarab() {
		super( Registries.ANCIENT_SCARAB );

		this.name( "AncientScarab" )
			.add( ExtraArchaeologyItem.create() );
	}
}
