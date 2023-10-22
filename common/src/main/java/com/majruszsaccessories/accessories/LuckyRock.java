package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningExtraItem;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

@AutoInstance
public class LuckyRock extends AccessoryHandler {
	public LuckyRock() {
		super( MajruszsAccessories.LUCKY_ROCK );

		this.add( MiningExtraItem.create( 0.03f ) )
			.add( MiningDropChance.create() )
			.add( TradeOffer.create( VillagerProfession.MASON, 5 ) );
	}

	static class MiningDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.0005f;

		public static ISupplier< AccessoryItem > create() {
			return MiningDropChance::new;
		}

		protected MiningDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			MiningExtraItem.OnStoneMined.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "mining_drop_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}
	}
}
