package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.common.Handler;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class TradeOffer extends AccessoryComponent {
	final VillagerProfession profession;
	final int tier;
	int price;

	public static ISupplier< AccessoryItem > create( VillagerProfession profession, int tier, int price ) {
		return handler->new TradeOffer( handler, profession, tier, price );
	}

	public static ISupplier< AccessoryItem > create( VillagerProfession profession, int tier ) {
		return TradeOffer.create( profession, tier, 7 );
	}

	protected TradeOffer( Handler< AccessoryItem > handler, VillagerProfession profession, int tier, int price ) {
		super( handler );

		this.profession = profession;
		this.tier = tier;
		this.price = price;

		Serializable config = handler.getConfig();
		config.defineInteger( "trade_price", ()->this.price, x->this.price = Range.of( 1, 32 ).clamp( x ) );
	}

	public MerchantOffer toMerchantOffer() {
		return new MerchantOffer( new ItemStack( this.getItem(), 1 ), new ItemStack( Items.EMERALD, this.getPrice() ), 2, 40, 0.05f ) {
			@Override
			public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
				return itemStack1.is( TradeOffer.this.getItem() ) && itemStack2.isEmpty();
			}
		};
	}

	public VillagerProfession getProfession() {
		return this.profession;
	}

	public int getTier() {
		return this.tier;
	}

	public int getPrice() {
		return this.price;
	}
}
