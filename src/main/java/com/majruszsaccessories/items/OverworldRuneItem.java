package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.list.*;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class OverworldRuneItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "overworld_rune" );

	public OverworldRuneItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "OverworldRune" );

			new MoreChestLoot( Registries.OVERWORLD_RUNE, ID, 1.5 );
			new FishingLuckBonus( Registries.OVERWORLD_RUNE, ID, 4 );
			new EnhanceTamedAnimal( Registries.OVERWORLD_RUNE, ID, 0.25 );
			new SpawnTwins( Registries.OVERWORLD_RUNE, ID, 0.3 );
			new ExtraStoneLoot( Registries.OVERWORLD_RUNE, ID, 0.04 );
			new EnhancePotions( Registries.OVERWORLD_RUNE, ID, 0.5, 1 );
			new DoubleCrops( Registries.OVERWORLD_RUNE, ID, 0.3 );
			new ReduceDamageReceived( Registries.OVERWORLD_RUNE, ID, 0.0625 );
		}
	}
}
