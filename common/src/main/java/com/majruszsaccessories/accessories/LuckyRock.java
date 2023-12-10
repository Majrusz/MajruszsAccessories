package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningExtraItem;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class LuckyRock extends AccessoryHandler {
	public LuckyRock() {
		super( MajruszsAccessories.LUCKY_ROCK, LuckyRock.class );

		this.add( MiningExtraItem.create( 0.03f ) )
			.add( MiningDropChance.create() )
			.add( TradeOffer.create() );
	}

	static class MiningDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.00075f;

		public static ISupplier< AccessoryItem > create() {
			return MiningDropChance::new;
		}

		protected MiningDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			MiningExtraItem.OnStoneMined.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.entity ) );

			handler.getConfig()
				.define( "mining_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
