package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.monster.Guardian;

import java.util.function.Supplier;

@AutoInstance
public class Dice extends BoosterBase {
	public Dice() {
		super( Registries.DICE );

		this.name( "Dice" )
			.add( AccessoryDropChance.create( 0.2 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends BoosterComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< BoosterItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.05, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Dice to drop from Guardian." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Guardian ) )
				.insertTo( group );
		}
	}
}
