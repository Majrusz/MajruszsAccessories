package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnPlayerWakedUp;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.SleepingBonuses;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class DreamCatcher extends AccessoryHandler {
	public DreamCatcher() {
		super( MajruszsAccessories.DREAM_CATCHER, DreamCatcher.class );

		this.add( SleepingBonuses.create( 1, 300 ) )
			.add( SleepingDropChance.create() )
			.add( TradeOffer.create( MajruszsAccessories.GAMBLING_CARD, 1 ) );
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

			handler.getConfig()
				.define( "sleeping_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnPlayerWakedUp data ) {
			this.spawnFlyingItem( data.getLevel(), data.player.position() );
		}
	}
}
