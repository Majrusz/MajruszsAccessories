package com.majruszsaccessories.boosters;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.EfficiencyNegation;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import net.minecraft.world.entity.monster.ElderGuardian;

@AutoInstance
public class Jadeite extends BoosterHandler {
	public Jadeite() {
		super( MajruszsAccessories.JADEITE );

		this.add( EfficiencyNegation.create() )
			.add( ElderGuardianDropChance.create() );
	}

	static class ElderGuardianDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.334f;

		public static ISupplier< BoosterItem > create() {
			return ElderGuardianDropChance::new;
		}

		protected ElderGuardianDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof ElderGuardian );

			Serializable config = handler.getConfig();
			config.defineFloat( "elder_guardian_drop_chance", ()->this.chance, x->this.chance = x );
		}
	}
}
