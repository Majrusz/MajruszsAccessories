package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnBabySpawned;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.BreedingTwins;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class IdolOfFertility extends AccessoryHandler {
	public IdolOfFertility() {
		super( MajruszsAccessories.IDOL_OF_FERTILITY, IdolOfFertility.class );

		this.add( BreedingTwins.create( 0.25f ) )
			.add( BreedingDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class BreedingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.01f;

		public static ISupplier< AccessoryItem > create() {
			return BreedingDropChance::new;
		}

		protected BreedingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnBabySpawned.listen( this::spawnTotem )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.predicate( data->data.player != null ) )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.player ) );

			handler.getConfig()
				.define( "breeding_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnTotem( OnBabySpawned data ) {
			this.spawnFlyingItem( data.getLevel(), data.parentA.position(), data.parentB.position() );
		}
	}
}
