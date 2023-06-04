package com.majruszsaccessories.accessories;

import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.client.ClientHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.ModConfigs;
import com.mlib.gamemodifiers.contexts.OnTradeSetup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class AccessoryBase {
	protected final List< AccessoryComponent > components = new ArrayList<>();
	protected final Supplier< AccessoryItem > item;
	protected final ConfigGroup group;

	public AccessoryBase( RegistryObject< AccessoryItem > item ) {
		this.item = item;
		this.group = ModConfigs.init( SERVER_CONFIG, item.getId().toString() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( OnAccessoryTooltip.is( this.item ) )
			.insertTo( this.group );

		OnTradeSetup.listen( this::addTrades )
			.insertTo( this.group );
	}

	public AccessoryBase name( String name ) {
		this.group.name( name );

		return this;
	}

	public AccessoryBase add( AccessoryComponent.ISupplier supplier ) {
		this.components.add( supplier.accept( this.item, this.group ) );

		return this;
	}

	public List< AccessoryComponent > getComponents() {
		return Collections.unmodifiableList( this.components );
	}

	private void addTooltip( OnAccessoryTooltip.Data data ) {
		this.components.stream()
			.map( AccessoryComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->ClientHelper.isShiftDown() ? provider.getDetailedTooltip( data.holder ) : provider.getTooltip( data.holder ) )
			.map( component->( Component )component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}

	private void addTrades( OnTradeSetup.Data data ) {
		this.components.stream()
			.filter( TradeOffer.class::isInstance )
			.map( TradeOffer.class::cast )
			.filter( offer->offer.getProfession() == data.profession )
			.forEach( offer->data.getTrades( offer.getTier() ).add( ( trader, random )->offer.toMerchantOffer() ) );
	}
}
