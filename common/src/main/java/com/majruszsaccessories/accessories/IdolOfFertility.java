package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.BreedingTwins;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnBabySpawned;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class IdolOfFertility extends AccessoryHandler {
	public IdolOfFertility() {
		super( MajruszsAccessories.IDOL_OF_FERTILITY );

		this.add( BreedingTwins.create( 0.25f ) )
			.add( BreedingDropChance.create() );
	}

	static class BreedingDropChance extends AccessoryComponent {
		float chance = 0.01f;

		public static ISupplier< AccessoryItem > create() {
			return BreedingDropChance::new;
		}

		protected BreedingDropChance( Handler< AccessoryItem > handler ) {
			super( handler );

			OnBabySpawned.listen( this::spawnTotem )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.predicate( data->data.player != null ) )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.player ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "breeding_drop_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}

		private void spawnTotem( OnBabySpawned data ) {
			this.spawnFlyingItem( data.getLevel(), data.parentA.position(), data.parentB.position() );
		}
	}
}
