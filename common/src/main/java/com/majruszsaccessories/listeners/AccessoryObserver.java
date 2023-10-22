package com.majruszsaccessories.listeners;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.AccessoryHolder;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemCrafted;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Priority;
import net.minecraft.server.level.ServerPlayer;

@AutoInstance
public class AccessoryObserver {
	public AccessoryObserver() {
		OnLootGenerated.listen( this::tryToGiveRandomBonus )
			.priority( Priority.LOWEST );

		OnItemCrafted.listen( this::tryToGiveRandomBonus )
			.addCondition( Condition.isLogicalServer() )
			.priority( Priority.LOWEST );
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
		if( holder.hasBooster() ) {
			MajruszsAccessories.HELPER.triggerAchievement( ( ServerPlayer )data.player, "booster_used" );
		}
	}
}
