package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.MiningExtraItem;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.mlib.annotation.AutoInstance;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class LuckyRock extends AccessoryHandler {
	public LuckyRock() {
		super( MajruszsAccessories.LUCKY_ROCK );

		this.add( MiningExtraItem.create( 0.03f ) )
			.add( MiningDropChance.create() );
	}

	static class MiningDropChance extends AccessoryComponent {
		float chance = 0.0005f;

		public static ISupplier< AccessoryItem > create() {
			return MiningDropChance::new;
		}

		protected MiningDropChance( Handler< AccessoryItem > handler ) {
			super( handler );

			MiningExtraItem.OnStoneMined.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "mining_drop_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}
	}
}
