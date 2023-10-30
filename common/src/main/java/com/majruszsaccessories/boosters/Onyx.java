package com.majruszsaccessories.boosters;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.EfficiencyBonus;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import net.minecraft.world.entity.monster.warden.Warden;

@AutoInstance
public class Onyx extends BoosterHandler {
	public Onyx() {
		super( MajruszsAccessories.ONYX );

		this.add( EfficiencyBonus.create( 0.09f ) )
			.add( WardenDropChance.create() );
	}

	static class WardenDropChance extends BonusComponent< BoosterItem > {
		float chance = 1.0f;

		public static ISupplier< BoosterItem > create() {
			return WardenDropChance::new;
		}

		protected WardenDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Warden );

			Serializable config = handler.getConfig();
			config.defineFloat( "warden_drop_chance", ()->this.chance, x->this.chance = x );
		}
	}
}
