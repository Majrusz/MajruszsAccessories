package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.StrongerPotions;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;
import net.minecraft.world.entity.npc.VillagerProfession;

@AutoInstance
public class SecretIngredient extends AccessoryHandler {
	public SecretIngredient() {
		super( MajruszsAccessories.SECRET_INGREDIENT );

		this.add( StrongerPotions.create( 0.6f, 1 ) )
			.add( TradeOffer.create( VillagerProfession.CLERIC, 5 ) );
	}
}
