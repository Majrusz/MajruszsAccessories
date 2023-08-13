package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.mlib.modhelper.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;

import java.util.function.Supplier;

@AutoInstance
public class Horseshoe extends BoosterBase {
	public Horseshoe() {
		super( Registries.HORSESHOE );

		this.name( "Horseshoe" )
			.add( LuckBonus.create( 1 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends BoosterComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< BoosterItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.334, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Horseshoe to drop from Skeleton Horseman." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Skeleton skeleton && skeleton.getRootVehicle() instanceof SkeletonHorse ) )
				.insertTo( group );
		}
	}
}
