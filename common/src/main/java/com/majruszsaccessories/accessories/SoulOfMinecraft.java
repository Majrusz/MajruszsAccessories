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
			.add( BrushingExtraItem.create( 0.24f ) )
			.add( SwimmingSpeedBonus.create( 0.3f ) )
			.add( FishingLuckBonus.create( 4 ) )
			.add( FishingLureBonus.create( 0.3f ) )
			.add( FishingExtraItems.create( 0.3f, 4 ) )
			.add( TradingDiscount.create( 0.18f ) )
			.add( SleepingBonuses.create( 2, 420 ) )
			.add( StrongerPotions.create( 0.4f, 1 ) )
			.add( MiningExtraItem.create( 0.05f ) )
			.add( MiningSpeedBonus.create( 0.15f ) )
			.add( MiningDurabilityBonus.create( 0.15f ) )
			.add( TamingStrongerAnimals.create( 0.3f ) )
			.add( BreedingTwins.create( 0.36f ) )
			.add( HarvestingDoubleCrops.create( 0.36f ) );
	}
}
