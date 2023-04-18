package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.*;
import com.mlib.annotations.AutoInstance;
import net.minecraft.world.entity.npc.VillagerProfession;

@AutoInstance
public class OverworldRune extends AccessoryBase {
	public OverworldRune() {
		super( Registries.OVERWORLD_RUNE );

		this.name( "OverworldRune" )
			.add( MoreChestLoot.create( 1.5 ) )
			.add( FishingLuckBonus.create( 4 ) )
			.add( EnhanceTamedAnimal.create( 0.25 ) )
			.add( SpawnTwins.create( 0.3 ) )
			.add( ExtraStoneLoot.create( 0.04 ) )
			.add( EnhancedPotions.create( 0.5, 1 ) )
			.add( DoubleCrops.create( 0.3 ) )
			.add( ReduceDamageReceived.create( 0.0625 ) )
			.add( TradeOffer.create( VillagerProfession.LIBRARIAN, 5, 17 ) );
	}
}
