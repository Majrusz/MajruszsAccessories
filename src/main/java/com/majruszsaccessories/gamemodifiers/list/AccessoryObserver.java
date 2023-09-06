package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.mlib.modhelper.AutoInstance;
import com.mlib.contexts.base.Priority;
import com.mlib.contexts.OnLoot;

@AutoInstance
public class AccessoryObserver {
	public AccessoryObserver() {
		OnLoot.listen( this::tryToGiveRandomBonus )
			.priority( Priority.LOWEST );
	}

	private void tryToGiveRandomBonus( OnLoot.Data data ) {
		data.generatedLoot.forEach( itemStack->{
			AccessoryHolder holder = AccessoryHolder.create( itemStack );
			if( holder.isValid() && !holder.hasBonusTag() ) {
				holder.setRandomBonus();
			}
		} );
	}
}
