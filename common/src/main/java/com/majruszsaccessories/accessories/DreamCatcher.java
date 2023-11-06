package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.SleepingBonuses;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnPlayerWakedUp;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class DreamCatcher extends AccessoryHandler {
	public DreamCatcher() {
		super( MajruszsAccessories.DREAM_CATCHER );

		this.add( SleepingBonuses.create( 1, 300 ) )
			.add( SleepingDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class SleepingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.1f;

		public static ISupplier< AccessoryItem > create() {
			return SleepingDropChance::new;
		}

		protected SleepingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnPlayerWakedUp.listen( this::spawnAccessory )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->!data.wasSleepStoppedManually )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.player ) );

			Serializable< ? > config = handler.getConfig();
			config.defineFloat( "sleeping_drop_chance", s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnPlayerWakedUp data ) {
			this.spawnFlyingItem( data.getLevel(), data.player.position() );
		}
	}
}
