package com.majruszsaccessories.common;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.tooltip.ITooltipProvider;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Component< Type extends Item > {
	protected final Handler< Type > handler;
	final List< ITooltipProvider > tooltipProviders = new ArrayList<>();

	public Component( Handler< Type > handler ) {
		this.handler = handler;
	}

	public Component< Type > addTooltip( String key, ITooltipProvider... providers ) {
		this.tooltipProviders.add( new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				return TextHelper.translatable( key, Stream.of( providers ).map( provider->provider.getTooltip( holder ) ).toArray() );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				return TextHelper.translatable( key, Stream.of( providers ).map( provider->provider.getDetailedTooltip( holder ) ).toArray() );
			}

			@Override
			public MutableComponent getRangeTooltip( AccessoryHolder holder ) {
				return TextHelper.translatable( key, Stream.of( providers ).map( provider->provider.getRangeTooltip( holder ) ).toArray() );
			}
		} );

		return this;
	}

	public List< ITooltipProvider > getTooltipProviders() {
		return Collections.unmodifiableList( this.tooltipProviders );
	}

	protected void addToGeneratedLoot( OnLootGenerated data ) {
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected void replaceGeneratedLoot( OnLootGenerated data ) {
		data.generatedLoot.clear();
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected ItemStack constructItemStack() {
		return new ItemStack( this.handler.getItem() );
	}

	protected Type getItem() {
		return this.handler.getItem();
	}

	@FunctionalInterface
	public interface ISupplier< Type extends Item > {
		Component< Type > apply( Handler< Type > handler );
	}
}
