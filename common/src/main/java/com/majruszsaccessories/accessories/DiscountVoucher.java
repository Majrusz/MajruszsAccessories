package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingExtraItems;
import com.majruszsaccessories.accessories.components.TradingDiscount;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class DiscountVoucher extends AccessoryHandler {
	public DiscountVoucher() {
		super( MajruszsAccessories.DISCOUNT_VOUCHER );

		this.add( TradingDiscount.create( 0.12f ) );
	}
}
