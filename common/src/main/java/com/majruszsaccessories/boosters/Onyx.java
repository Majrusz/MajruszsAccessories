package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.EfficiencyBonus;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.warden.Warden;

@AutoInstance
public class Onyx extends BoosterHandler {
	public Onyx() {
		super( MajruszsAccessories.ONYX, Onyx.class );

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

			handler.getConfig()
				.define( "warden_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
