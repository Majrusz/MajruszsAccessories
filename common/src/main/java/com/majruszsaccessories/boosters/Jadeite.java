package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.EfficiencyNegation;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.ElderGuardian;

@AutoInstance
public class Jadeite extends BoosterHandler {
	public Jadeite() {
		super( MajruszsAccessories.JADEITE, Jadeite.class );

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

			handler.getConfig()
				.define( "elder_guardian_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
