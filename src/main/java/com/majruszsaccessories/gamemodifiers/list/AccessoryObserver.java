package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import com.mlib.gamemodifiers.data.OnLootData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import com.mlib.gamemodifiers.parameters.Priority;

public class AccessoryObserver extends GameModifier {
	public AccessoryObserver() {
		super( Registries.Modifiers.DEFAULT_GROUP, "AccessoryObserver", "" );

		this.addContext( new OnLootContext( this::tryToSetupAccessories, new ContextParameters( Priority.LOWEST, "", "" ) ) );
	}

	private void tryToSetupAccessories( OnLootData data ) {
		data.generatedLoot.forEach( itemStack->{
			if( itemStack.getItem() instanceof AccessoryItem ) {
				AccessoryHandler.setup( itemStack );
			}
		} );
	}
}
