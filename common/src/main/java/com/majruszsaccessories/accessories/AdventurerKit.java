package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class AdventurerKit extends AccessoryHandler {
	public AdventurerKit() {
		super( MajruszsAccessories.ADVENTURER_KIT );

		this.add( MoreChestLoot.create( 1.2f ) )
			.add( AnyChestDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class AnyChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.025f;

		public static ISupplier< AccessoryItem > create() {
			return AnyChestDropChance::new;
		}

		protected AnyChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			MoreChestLoot.OnChestOpened.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "any_chest_spawn_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}
	}
}
