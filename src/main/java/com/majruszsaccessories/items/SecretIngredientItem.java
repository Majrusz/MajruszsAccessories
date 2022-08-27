package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.EnhancePotions;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class SecretIngredientItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "secret_ingredient" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "SecretIngredient", "" ) );

	public static Supplier< SecretIngredientItem > create() {
		GameModifiersHolder< SecretIngredientItem > holder = AccessoryItem.newHolder( ID, SecretIngredientItem::new );
		holder.addModifier( EnhancePotions::new );
		holder.addModifier( AddDropChance::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLootContext onLoot = new OnLootContext( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.025, "drop_chance", "Chance for Secret Ingredient to drop from Witch." ) )
				.addCondition( OnLootContext.HAS_LAST_DAMAGE_PLAYER )
				.addCondition( data->data.entity instanceof Witch );

			this.addContext( onLoot );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.CLERIC, 5 );
		}
	}
}
