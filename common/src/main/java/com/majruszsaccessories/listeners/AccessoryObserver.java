package com.majruszsaccessories.listeners;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.events.OnEntityTicked;
import com.majruszlibrary.events.OnItemCrafted;
import com.majruszlibrary.events.OnItemEquipped;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Priority;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.mixininterfaces.IMixinLivingEntity;

@AutoInstance
public class AccessoryObserver {
	public AccessoryObserver() {
		OnItemEquipped.listen( this::tryToGiveRandomBonus )
			.addCondition( data->data.to.getItem() instanceof AccessoryItem )
			.priority( Priority.LOW );

		OnLootGenerated.listen( this::tryToGiveRandomBonus )
			.priority( Priority.LOW );

		OnItemCrafted.listen( this::tryToGiveRandomBonus )
			.addCondition( Condition.isLogicalServer() )
			.priority( Priority.LOW );

		OnEntityTicked.listen( this::updateHolder );
	}

	private void tryToGiveRandomBonus( OnItemEquipped data ) {
		// compatibility fix for mods that modify crafting behaviour etc.
		AccessoryHolder holder = AccessoryHolder.create( data.to );
		if( holder.isValid() && !holder.hasBonusDefined() ) {
			holder.setRandomBonus();
		}
	}

	private void tryToGiveRandomBonus( OnLootGenerated data ) {
		data.generatedLoot.forEach( itemStack->{
			AccessoryHolder holder = AccessoryHolder.create( itemStack );
			if( holder.isValid() && !holder.hasBonusDefined() ) {
				holder.setRandomBonus();
			}
		} );
	}

	private void tryToGiveRandomBonus( OnItemCrafted data ) {
		AccessoryHolder holder = AccessoryHolder.create( data.itemStack );
		if( holder.hasBonusRangeDefined() && !holder.hasBonusDefined() ) {
			holder.setRandomBonus();
		}
	}

	private void updateHolder( OnEntityTicked data ) {
		( ( IMixinLivingEntity )data.entity ).majruszsaccessories$setAccessoryHolder( AccessoryHolder.find( data.entity ) );
	}
}
