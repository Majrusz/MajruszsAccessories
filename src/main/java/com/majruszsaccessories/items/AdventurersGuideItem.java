package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.MoreChestLoot;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLoot;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class AdventurersGuideItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "adventurers_guide" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "AdventurersGuide", "" ) );

	public static Supplier< AdventurersGuideItem > create() {
		GameModifiersHolder< AdventurersGuideItem > holder = AccessoryItem.newHolder( ID, AdventurersGuideItem::new );
		holder.addModifier( MoreChestLoot::new );
		holder.addModifier( AddDropChance::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLoot.Context onLoot = MoreChestLoot.lootContext( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.Chance<>( 0.025, "spawn_chance", "Chance for Adventurer's Guide to spawn in any chest." ) );

			this.addContext( onLoot );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.CARTOGRAPHER, 5 );
		}
	}
}
