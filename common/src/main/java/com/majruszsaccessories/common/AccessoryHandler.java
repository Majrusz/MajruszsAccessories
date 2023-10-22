package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.contexts.OnTradesInitialized;
import com.mlib.registry.RegistryObject;

public class AccessoryHandler extends BonusHandler< AccessoryItem > {
	public AccessoryHandler( RegistryObject< AccessoryItem > item ) {
		super( item, MajruszsAccessories.CONFIG.accessories, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnTradesInitialized.listen( this::addTrades );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->AccessoryHolder.create( data.itemStack ).hasBooster() );
	}
}
