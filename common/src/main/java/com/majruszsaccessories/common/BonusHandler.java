package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.mlib.client.ClientHelper;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.contexts.OnTradesInitialized;
import com.mlib.data.Serializable;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BonusHandler< Type extends Item > {
	protected final List< BonusComponent< Type > > components = new ArrayList<>();
	protected final Supplier< Type > item;
	protected final Serializable config;
	protected final String id;

	public BonusHandler( Supplier< Type > item, Serializable config, String id ) {
		this.item = item;
		this.config = new Serializable();
		this.id = id;

		config.defineCustom( id, ()->this.config );
	}

	public BonusHandler< Type > add( BonusComponent.ISupplier< Type > supplier ) {
		this.components.add( supplier.apply( this ) );

		return this;
	}

	public List< ? extends BonusComponent< Type > > getComponents() {
		return Collections.unmodifiableList( this.components );
	}

	public Type getItem() {
		return this.item.get();
	}

	public Serializable getConfig() {
		return this.config;
	}

	public String getId() {
		return this.id;
	}

	protected void addTooltip( OnAccessoryTooltip data ) {
		this.components.stream()
			.map( BonusComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->{
				if( data.holder.hasBonusRangeDefined() ) {
					return provider.getRangeTooltip( data.holder );
				} else if( ClientHelper.isShiftDown() ) {
					return provider.getDetailedTooltip( data.holder );
				} else {
					return provider.getTooltip( data.holder );
				}
			} )
			.map( component->component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}

	protected void addTrades( OnTradesInitialized data ) {
		this.components.stream()
			.filter( TradeOffer.class::isInstance )
			.map( TradeOffer.class::cast )
			.filter( offer->offer.getProfession() == data.profession )
			.forEach( offer->data.getTrades( offer.getTier() ).add( ( trader, random )->offer.toMerchantOffer() ) );
	}

	protected void addBoosterIcon( OnItemDecorationsRendered data ) {
		data.gui.renderItem( new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY.get() ), data.x, data.y );
	}
}