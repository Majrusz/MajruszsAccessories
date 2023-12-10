package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.ExperienceBonus;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.Vex;

@AutoInstance
public class OwlFeather extends BoosterHandler {
	public OwlFeather() {
		super( MajruszsAccessories.OWL_FEATHER, OwlFeather.class );

		this.add( ExperienceBonus.create( 0.15f ) )
			.add( VexDropChance.create() )
			.add( TradeOffer.create( 1 ) );
	}

	static class VexDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.1f;

		public static ISupplier< BoosterItem > create() {
			return VexDropChance::new;
		}

		protected VexDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Vex );

			handler.getConfig()
				.define( "vex_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
