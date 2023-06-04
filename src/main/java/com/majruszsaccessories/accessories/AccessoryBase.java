package com.majruszsaccessories.accessories;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.client.ClientHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

	public < Type > List< Type > getComponents( Class< Type > clazz ) {
		return Stream.of( this.components )
			.filter( clazz::isInstance )
			.map( clazz::cast )
			.toList();
	}

	private void addTooltip( OnAccessoryTooltip.Data data ) {
		this.components.stream()
			.map( AccessoryComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->ClientHelper.isShiftDown() ? provider.getDetailedTooltip( data.holder ) : provider.getTooltip( data.holder ) )
			.map( component->( Component )component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}
}
