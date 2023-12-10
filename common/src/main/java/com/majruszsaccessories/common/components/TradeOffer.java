package com.majruszsaccessories.common.components;

import com.majruszlibrary.events.OnWanderingTradesUpdated;
import com.majruszlibrary.math.Random;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.items.CardItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TradeOffer< Type extends Item > extends BonusComponent< Type > {
	private static final List< TradeOffer< ? > > OFFERS = new ArrayList<>();
	private final Supplier< CardItem > card;
	private final int count;

	static {
		OnWanderingTradesUpdated.listen( TradeOffer::addTrades );
	}

	public static < Type extends Item > ISupplier< Type > create( int count ) {
		return handler->new TradeOffer<>( handler, count );
	}

	protected TradeOffer( BonusHandler< Type > handler, int count ) {
		super( handler );

		this.card = ()->Random.next( List.of( MajruszsAccessories.GAMBLING_CARD, MajruszsAccessories.REMOVAL_CARD, MajruszsAccessories.REVERSE_CARD ) ).get();
		this.count = count;

		OFFERS.add( this );
	}

	public MerchantOffer toMerchantOffer() {
		return new MerchantOffer( new ItemStack( this.getItem(), 1 ), new ItemStack( this.card.get(), this.count ), 2, 40, 0.05f ) {
			@Override
			public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
				return itemStack1.is( TradeOffer.this.getItem() ) && itemStack2.isEmpty();
			}
		};
	}

	private static void addTrades( OnWanderingTradesUpdated data ) {
		data.offers.addAll( Random.next( OFFERS.stream().map( TradeOffer::toMerchantOffer ).toList(), 5 ) );
	}
}
