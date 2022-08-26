package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.SpawnTwins;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnBabySpawnContext;
import com.mlib.gamemodifiers.data.OnBabySpawnData;
import com.mlib.levels.LevelHelper;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class IdolOfFertilityItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "idol_of_fertility" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "IdolOfFertility", "" ) );

	public static Supplier< IdolOfFertilityItem > create() {
		GameModifiersHolder< IdolOfFertilityItem > holder = new GameModifiersHolder<>( ID, IdolOfFertilityItem::new );
		holder.addModifier( SpawnTwins::new );
		holder.addModifier( AddDropChance::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnBabySpawnContext onBabySpawn = new OnBabySpawnContext( this::spawnTotem );
			onBabySpawn.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.0005, "drop_chance", "Chance for Idol of Fertility to drop when breeding animals." ) );

			this.addContext( onBabySpawn );
		}

		private void spawnTotem( OnBabySpawnData data ) {
			this.spawnFlyingItem( data.level, data.parentA.position(), data.parentB.position() );
		}
	}
}
