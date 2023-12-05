package com.majruszsaccessories.listeners;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.events.OnEntityTicked;
import com.majruszlibrary.events.OnItemCrafted;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Priority;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.mixininterfaces.IMixinLivingEntity;
import net.minecraft.server.level.ServerPlayer;

@AutoInstance
public class AccessoryObserver {
	public AccessoryObserver() {
		OnLootGenerated.listen( this::tryToGiveRandomBonus )
			.priority( Priority.LOWEST );

		OnItemCrafted.listen( this::tryToGiveRandomBonus )
			.addCondition( Condition.isLogicalServer() )
			.priority( Priority.LOWEST );

		OnEntityTicked.listen( this::updateHolder );
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
		if( holder.hasAnyBooster() ) {
			MajruszsAccessories.HELPER.triggerAchievement( ( ServerPlayer )data.player, "booster_used" );
		}
	}

	private void updateHolder( OnEntityTicked data ) {
		( ( IMixinLivingEntity )data.entity ).majruszsaccessories$setAccessoryHolder( AccessoryHolder.find( data.entity ) );
	}
}
