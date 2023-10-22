package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.StrongerPotions;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class SecretIngredient extends AccessoryHandler {
	public SecretIngredient() {
		super( MajruszsAccessories.SECRET_INGREDIENT );

		this.add( StrongerPotions.create( 0.6f, 1 ) );
	}
}
