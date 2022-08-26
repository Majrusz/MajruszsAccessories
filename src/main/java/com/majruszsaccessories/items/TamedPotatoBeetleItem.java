package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.DoubleCrops;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class TamedPotatoBeetleItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "tamed_potato_beetle" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "TamedPotatoBeetle", "" ) );

	public static Supplier< TamedPotatoBeetleItem > create() {
		GameModifiersHolder< TamedPotatoBeetleItem > holder = new GameModifiersHolder<>( ID, TamedPotatoBeetleItem::new );
		holder.addModifier( DoubleCrops::new );
		holder.addModifier( AddDropChance::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLootContext onLoot = new OnLootContext( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.002, "drop_chance", "Chance for Tamed Potato Beetle to drop from crops." ) )
				.addCondition( OnLootContext.HAS_ORIGIN )
				.addCondition( data->data.blockState != null && BlockHelper.isCropAtMaxAge( data.blockState ) )
				.addCondition( data->data.entity instanceof LivingEntity );

			this.addContext( onLoot );
		}
	}
}
