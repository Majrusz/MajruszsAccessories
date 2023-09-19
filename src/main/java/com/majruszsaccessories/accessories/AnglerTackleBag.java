package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.ExtraFishingItems;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.world.entity.npc.VillagerProfession;

@AutoInstance
public class AnglerTackleBag extends AccessoryBase {
	public AnglerTackleBag() {
		super( Registries.ANGLER_TACKLE_BAG );

		this.name( "AnglerTackleBag" )
			.add( FishingLuckBonus.create( 3 ) )
			.add( FishingLureBonus.create( 0.25 ) )
			.add( ExtraFishingItems.create( 0.25, 3 ) );
	}
}
