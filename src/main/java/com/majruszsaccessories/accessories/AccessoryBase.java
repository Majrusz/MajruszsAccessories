package com.majruszsaccessories.accessories;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.components.AccessoryComponent;
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
	}

	public AccessoryBase name( String name ) {
		this.group.name( name );

		return this;
	}

	public AccessoryBase add( AccessoryComponent.ISupplier supplier ) {
		this.components.add( supplier.accept( this.item, this.group ) );

		return this;
	}

	public List< Component > buildTooltip( AccessoryHolder holder ) {
		return this.components.stream()
			.map( AccessoryComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->ClientHelper.isShiftDown() ? provider.getDetailedTooltip( holder ) : provider.getTooltip( holder ) )
			.map( component->( Component )component.withStyle( ChatFormatting.GRAY ) )
			.toList();
	}

	public boolean is( AccessoryItem item ) {
		return this.item.get().equals( item );
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
}
