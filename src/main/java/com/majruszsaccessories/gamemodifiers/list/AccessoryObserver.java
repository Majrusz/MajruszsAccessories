package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHolder;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Priority;
import com.mlib.gamemodifiers.contexts.OnLoot;

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
