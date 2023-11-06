package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.accessories.components.TradingDiscount;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemTraded;
import com.mlib.data.Serializable;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;

@AutoInstance
public class DiscountVoucher extends AccessoryHandler {
	public DiscountVoucher() {
		super( MajruszsAccessories.DISCOUNT_VOUCHER );

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

			Serializable< ? > config = handler.getConfig();
			config.defineFloat( "trading_drop_chance", s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnItemTraded data ) {
			AnyPos pos = AnyPos.from( data.villager.position() );

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
