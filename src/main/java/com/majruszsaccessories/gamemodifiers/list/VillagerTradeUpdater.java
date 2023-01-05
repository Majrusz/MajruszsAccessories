package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.IAccessoryOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnTradeSetup;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;

@AutoInstance
public class VillagerTradeUpdater extends GameModifier {
	public VillagerTradeUpdater() {
		super( Registries.Modifiers.DEFAULT_GROUP );

		new OnTradeSetup.Context( this::addTrades )
			.insertTo( this );

		this.name( "VillagerTradeUpdater" );
	}

	private void addTrades( OnTradeSetup.Data data ) {
		BaseOffer.OFFERS.forEach( offer->this.addTrades( data, offer ) );
	}

	private void addTrades( OnTradeSetup.Data data, IAccessoryOffer offer ) {
		if( offer.getProfession() != data.profession ) {
			return;
		}

		List< VillagerTrades.ItemListing > trades = data.getTrades( offer.getTier() );
		trades.add( ( trader, random )->new AccessoryMerchantOffer( offer ) );
	}

	static class AccessoryMerchantOffer extends MerchantOffer {
		final AccessoryItem item;

		public AccessoryMerchantOffer( IAccessoryOffer offer ) {
			super( new ItemStack( offer.getItem(), 1 ), new ItemStack( Items.EMERALD, offer.getPrice() ), 2, 40, 0.05f );
			this.item = offer.getItem();
		}

		@Override
		public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
			return itemStack1.is( this.item ) && itemStack2.isEmpty();
		}
	}
}
