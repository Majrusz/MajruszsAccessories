package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnWanderingTradesUpdated;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TradeOffer extends BonusComponent< AccessoryItem > {
	private static final List< TradeOffer > OFFERS = new ArrayList<>();
	int price;

	public static ISupplier< AccessoryItem > create( int price ) {
		return handler->new TradeOffer( handler, price );
	}

	protected TradeOffer( BonusHandler< AccessoryItem > handler, int price ) {
		super( handler );

		this.price = price;

		handler.getConfig()
			.define( "trade_price", Reader.integer(), s->this.price, ( s, v )->this.price = Range.of( 1, 32 ).clamp( v ) );

		OFFERS.add( this );
	}

	public MerchantOffer toMerchantOffer() {
		return new MerchantOffer( new ItemStack( this.getItem(), 1 ), new ItemStack( Items.EMERALD, this.getPrice() ), 2, 40, 0.05f ) {
			@Override
			public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
				return itemStack1.is( TradeOffer.this.getItem() ) && itemStack2.isEmpty();
			}
		};
	}

	public int getPrice() {
		return this.price;
	}

	@AutoInstance
	public static class Updater {
		public Updater() {
			OnWanderingTradesUpdated.listen( this::addRandom );
		}

		private void addRandom( OnWanderingTradesUpdated data ) {
			data.offers.addAll( this.getOffers( offer->offer.getItem().getBoosterSlotsCount() == 1, 3 ) );
			data.offers.addAll( this.getOffers( offer->offer.getItem().getBoosterSlotsCount() == 2, 1 ) );
		}

		private List< MerchantOffer > getOffers( Predicate< TradeOffer > predicate, int count ) {
			return Random.next( OFFERS.stream().filter( predicate ).map( TradeOffer::toMerchantOffer ).toList(), count );
		}
	}
}
