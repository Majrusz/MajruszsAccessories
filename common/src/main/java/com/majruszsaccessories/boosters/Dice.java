package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.Guardian;

@AutoInstance
public class Dice extends BoosterHandler {
	public Dice() {
		super( MajruszsAccessories.DICE, Dice.class );

		this.add( AccessoryDropChance.create( 0.15f ) )
			.add( GuardianDropChance.create() )
			.add( TradeOffer.create() );
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

			handler.getConfig()
				.define( "guardian_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
