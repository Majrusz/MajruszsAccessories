package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.EnhancePotions;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class SecretIngredientItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "secret_ingredient" );

	public SecretIngredientItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "SecretIngredient" );

			new EnhancePotions( Registries.SECRET_INGREDIENT, ID );
			new AddDropChance( Registries.SECRET_INGREDIENT, ID );
			new TradeOffer( Registries.SECRET_INGREDIENT, ID );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new OnLoot.Context( this::addToGeneratedLoot )
				.addCondition( new Condition.IsServer<>() )
				.addCondition( new DropChance( 0.025 ) )
				.addCondition( OnLoot.HAS_LAST_DAMAGE_PLAYER )
				.addCondition( data->data.entity instanceof Witch )
				.insertTo( this );
		}

		static class DropChance extends Condition.Chance< OnLoot.Data > {
			public DropChance( double chance ) {
				super( chance );

				this.chance.name( "drop_chance" ).comment( "Chance for Secret Ingredient to drop from Witch." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.CLERIC, 5 );
		}
	}
}
