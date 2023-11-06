package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class MinerGuide extends AccessoryHandler {
	public MinerGuide() {
		super( MajruszsAccessories.MINER_GUIDE );

		this.add( MiningSpeedBonus.create( 0.1f ) )
			.add( UndergroundChestDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class UndergroundChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.05f;

		public static ISupplier< AccessoryItem > create() {
			return UndergroundChestDropChance::new;
		}

		protected UndergroundChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( data->data.origin != null && data.origin.y < 50.0f )
				.addCondition( data->data.lootId.toString().contains( "chest" ) )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.entity ) );

			Serializable< ? > config = handler.getConfig();
			config.defineFloat( "underground_chest_spawn_chance", s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
