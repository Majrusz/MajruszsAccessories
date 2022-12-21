package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.parameters.Priority;

@AutoInstance
public class AccessoryObserver extends GameModifier {
	public AccessoryObserver() {
		super( Registries.Modifiers.DEFAULT_GROUP, "AccessoryObserver", "" );

		this.addContext( new OnLoot.Context( this::tryToSetupAccessories ).priority( Priority.LOWEST ) );
	}

	private void tryToSetupAccessories( OnLoot.Data data ) {
		data.generatedLoot.forEach( itemStack->{
			if( itemStack.getItem() instanceof AccessoryItem ) {
				AccessoryHandler.setup( itemStack, AccessoryHandler.randomBonus() );
			}
		} );
	}
}
