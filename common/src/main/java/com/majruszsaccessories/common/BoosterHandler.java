package com.majruszsaccessories.common;

import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.events.OnItemDecorationsRendered;
import com.majruszlibrary.registry.RegistryObject;
import com.majruszsaccessories.config.Config;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.contexts.OnBoosterTooltip;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BoosterHandler extends BonusHandler< BoosterItem > {
	public BoosterHandler( RegistryObject< BoosterItem > item, Class< ? extends BoosterHandler > clazz ) {
		super( item, clazz, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.hasBooster( this.getItem() ) );

		OnBoosterTooltip.listen( this::addTooltip )
			.addCondition( data->data.booster.equals( this.getItem() ) );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->data.itemStack.is( this.getItem() ) );

		Serializables.getStatic( Config.Boosters.class )
			.define( item.getId(), clazz );
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
