package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.events.OnAccessoryCompatibilityGet;
import com.majruszsaccessories.items.AccessoryItem;

import java.util.function.Supplier;

public class AccessoryIncompatibility extends BonusComponent< AccessoryItem > {
	public static ISupplier< AccessoryItem > create( Supplier< AccessoryItem > accessory ) {
		return handler->new AccessoryIncompatibility( handler, accessory );
	}

	protected AccessoryIncompatibility( BonusHandler< AccessoryItem > handler, Supplier< AccessoryItem > accessory ) {
		super( handler );

		OnAccessoryCompatibilityGet.listen( OnAccessoryCompatibilityGet::makeIncompatible )
			.addCondition( data->{
				return data.a.equals( this.getItem() ) && data.b.equals( accessory.get() )
					|| data.a.equals( accessory.get() ) && data.b.equals( this.getItem() );
			} );
	}
}
