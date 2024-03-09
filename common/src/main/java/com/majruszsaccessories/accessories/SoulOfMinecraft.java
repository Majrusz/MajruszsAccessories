package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.*;
import com.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class SoulOfMinecraft extends AccessoryHandler {
	public SoulOfMinecraft() {
		super( MajruszsAccessories.SOUL_OF_MINECRAFT, SoulOfMinecraft.class );

		this.add( MoreChestLoot.create( 1.8f ) )
			.add( SwimmingSpeedBonus.create( 0.3f ) )
			.add( FishingLuckBonus.create( 3.5f ) )
			.add( FishingLureBonus.create( 0.3f ) )
			.add( FishingExtraTreasure.create( 0.07f ) )
			.add( TradingDiscount.create( 0.18f ) )
			.add( SleepingBonuses.create( 1.5f, 420 ) )
			.add( StrongerPotions.create( 0.4f, 1.4f ) )
			.add( MiningExtraItem.create( 0.05f ) )
			.add( MiningSpeedBonus.create( 0.15f ) )
			.add( MiningDurabilityBonus.create( 0.15f ) )
			.add( TamingStrongerAnimals.create( 0.3f ) )
			.add( BreedingTwins.create( 0.36f ) )
			.add( HarvestingDoubleCrops.create( 0.36f ) );
	}
}
