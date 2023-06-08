package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.boosters.components.ExperienceBonus;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.monster.Vex;

import java.util.function.Supplier;

@AutoInstance
public class OwlFeather extends BoosterBase {
	public OwlFeather() {
		super( Registries.OWL_FEATHER );

		this.name( "OwlFeather" )
			.add( ExperienceBonus.create( 0.15 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends BoosterComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< BoosterItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.1, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Owl Feather to drop from Vex." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Vex ) )
				.insertTo( group );
		}
	}
}
