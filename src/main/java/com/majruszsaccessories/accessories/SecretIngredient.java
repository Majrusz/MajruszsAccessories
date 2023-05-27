package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.AccessoryComponent;
import com.majruszsaccessories.components.EnhancedPotions;
import com.majruszsaccessories.components.TradeOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class SecretIngredient extends AccessoryBase {
	public SecretIngredient() {
		super( Registries.SECRET_INGREDIENT );

		this.name( "SecretIngredient" )
			.add( EnhancedPotions.create() )
			.add( TradeOffer.create( VillagerProfession.CLERIC, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.025, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Secret Ingredient to drop from Witch." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Witch ) )
				.insertTo( group );
		}
	}
}
