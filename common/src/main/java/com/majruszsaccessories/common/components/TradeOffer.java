package com.majruszsaccessories.common.components;

import com.majruszlibrary.events.OnWanderingTradesUpdated;
import com.majruszlibrary.math.Random;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.items.CardItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TradeOffer< Type extends Item > extends BonusComponent< Type > {
	private static final List< TradeOffer< ? > > OFFERS = new ArrayList<>();
	private final Supplier< CardItem > card;
	private final int count;

	static {
		OnWanderingTradesUpdated.listen( TradeOffer::addTrades );
	}

	public static < Type extends Item > ISupplier< Type > create( Supplier< CardItem > card, int count ) {
		return handler->new TradeOffer<>( handler, card, count );
	}

	protected TradeOffer( BonusHandler< Type > handler, Supplier< CardItem > card, int count ) {
		super( handler );

		this.card = card;
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
		data.offers.addAll( TradeOffer.getOffers( offer->offer.getItem() instanceof AccessoryItem, 3 ) );
		data.offers.addAll( TradeOffer.getOffers( offer->offer.getItem() instanceof BoosterItem, 1 ) );
	}

	private static List< MerchantOffer > getOffers( Predicate< TradeOffer > predicate, int count ) {
		return Random.next( OFFERS.stream().filter( predicate ).map( TradeOffer::toMerchantOffer ).toList(), count );
	}
}
