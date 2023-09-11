package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.boosters.components.EfficiencyBonus;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnLoot;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.world.entity.monster.warden.Warden;

import java.util.function.Supplier;

@AutoInstance
public class Onyx extends BoosterBase {
	public Onyx() {
		super( Registries.ONYX );

		this.name( "Onyx" )
			.add( EfficiencyBonus.create( 0.05 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends BoosterComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< BoosterItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 1.0, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Onyx to drop from Warden." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Warden ) )
				.insertTo( group );
		}
	}
}
