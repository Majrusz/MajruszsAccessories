package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.registry.RegistryObject;

public class AccessoryHandler extends Handler< AccessoryItem > {
	public AccessoryHandler( RegistryObject< AccessoryItem > item ) {
		super( item, MajruszsAccessories.CONFIG.accessories, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->AccessoryHolder.create( data.itemStack ).hasBooster() );
	}
}
