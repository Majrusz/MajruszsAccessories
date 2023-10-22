package com.majruszsaccessories.boosters;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import net.minecraft.world.entity.monster.Guardian;

@AutoInstance
public class Dice extends BoosterHandler {
	public Dice() {
		super( MajruszsAccessories.DICE );

		this.add( AccessoryDropChance.create( 0.2f ) )
			.add( GuardianDropChance.create() );
	}

	static class GuardianDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.05f;

		public static ISupplier< BoosterItem > create() {
			return GuardianDropChance::new;
		}

		protected GuardianDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Guardian );

			Serializable config = handler.getConfig();
			config.defineFloat( "guardian_drop_chance", ()->this.chance, x->this.chance = x );
		}
	}
}
