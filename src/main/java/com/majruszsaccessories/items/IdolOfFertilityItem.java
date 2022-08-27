package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.SpawnTwins;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnBabySpawn;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class IdolOfFertilityItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "idol_of_fertility" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "IdolOfFertility", "" ) );

	public static Supplier< IdolOfFertilityItem > create() {
		GameModifiersHolder< IdolOfFertilityItem > holder = AccessoryItem.newHolder( ID, IdolOfFertilityItem::new );
		holder.addModifier( SpawnTwins::new );
		holder.addModifier( AddDropChance::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnBabySpawn.Context onBabySpawn = SpawnTwins.babySpawnContext( this::spawnTotem );
			onBabySpawn.addCondition( new Condition.Chance( 0.005, "drop_chance", "Chance for Idol of Fertility to drop when breeding animals." ) );

			this.addContext( onBabySpawn );
		}

		private void spawnTotem( OnBabySpawn.Data data ) {
			this.spawnFlyingItem( data.level, data.parentA.position(), data.parentB.position() );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.BUTCHER, 5 );
		}
	}
}
