package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnItemTraded;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.accessories.components.TradingDiscount;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class DiscountVoucher extends AccessoryHandler {
	public DiscountVoucher() {
		super( MajruszsAccessories.DISCOUNT_VOUCHER, DiscountVoucher.class );

		this.add( TradingDiscount.create( 0.12f ) )
			.add( TradingDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class TradingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.01f;

		public static ISupplier< AccessoryItem > create() {
			return TradingDropChance::new;
		}

		protected TradingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnItemTraded.listen( this::spawnAccessory )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.player ) );

			handler.getConfig()
				.define( "trading_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnItemTraded data ) {
			AnyPos pos = AnyPos.from( data.villager.position() );

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
