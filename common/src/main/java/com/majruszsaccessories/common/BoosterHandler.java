package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.contexts.OnBoosterTooltip;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.registry.RegistryObject;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BoosterHandler extends BonusHandler< BoosterItem > {
	public BoosterHandler( RegistryObject< BoosterItem > item ) {
		super( item, MajruszsAccessories.CONFIG.boosters, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.hasBooster( this.getItem() ) );

		OnBoosterTooltip.listen( this::addTooltip )
			.addCondition( data->data.booster.equals( this.getItem() ) );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->data.itemStack.is( this.getItem() ) );
	}

	protected void addTooltip( OnBoosterTooltip data ) {
		this.components.stream()
			.map( BonusComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->provider.getTooltip( AccessoryHolder.create( ItemStack.EMPTY ) ) )
			.map( component->component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}
}
