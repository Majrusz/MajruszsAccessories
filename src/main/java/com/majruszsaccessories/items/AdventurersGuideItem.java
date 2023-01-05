package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.MoreChestLoot;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class AdventurersGuideItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "adventurers_guide" );

	public AdventurersGuideItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "AdventurersGuide" );

			new MoreChestLoot( Registries.ADVENTURERS_GUIDE, ID );
			new AddDropChance( Registries.ADVENTURERS_GUIDE, ID );
			new TradeOffer( Registries.ADVENTURERS_GUIDE, ID );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new MoreChestLoot.OnChestContext( this::addToGeneratedLoot )
				.addCondition( new SpawnChance( 0.025 ) )
				.insertTo( this );
		}

		static class SpawnChance extends Condition.Chance< OnLoot.Data > {
			public SpawnChance( double chance ) {
				super( chance );

				this.chance.name( "spawn_chance" ).comment( "Chance for Adventurer's Guide to spawn in any chest." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.CARTOGRAPHER, 5 );
		}
	}
}
