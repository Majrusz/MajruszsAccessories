package com.majruszsaccessories.common;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.tooltip.ITooltipProvider;
import com.mlib.contexts.OnLoot;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ComponentBase< ItemType extends Item > {
	protected final Supplier< ItemType > item;
	final List< ITooltipProvider > tooltipProviders = new ArrayList<>();

	public ComponentBase( Supplier< ItemType > item ) {
		this.item = item;
	}

	public ComponentBase< ItemType > addTooltip( String key, ITooltipProvider... providers ) {
		this.tooltipProviders.add( new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				return Component.translatable( key, Stream.of( providers ).map( provider->provider.getTooltip( holder ) ).toArray() );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				return Component.translatable( key, Stream.of( providers ).map( provider->provider.getDetailedTooltip( holder ) ).toArray() );
			}
		} );

		return this;
	}

	public List< ITooltipProvider > getTooltipProviders() {
		return Collections.unmodifiableList( this.tooltipProviders );
	}

	protected void addToGeneratedLoot( OnLoot.Data data ) {
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected void replaceGeneratedLoot( OnLoot.Data data ) {
		data.generatedLoot.clear();
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected ItemStack constructItemStack() {
		return new ItemStack( this.item.get() );
	}
}
