package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class AdventurerKit extends AccessoryHandler {
	public AdventurerKit() {
		super( MajruszsAccessories.ADVENTURER_KIT, AdventurerKit.class );

		this.add( MoreChestLoot.create( 1.2f ) )
			.add( AnyChestDropChance.create() )
			.add( TradeOffer.create( MajruszsAccessories.GAMBLING_CARD, 1 ) );
	}

	static class AnyChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.025f;

		public static ISupplier< AccessoryItem > create() {
			return AnyChestDropChance::new;
		}

		protected AnyChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			MoreChestLoot.OnChestOpened.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->MoreChestLoot.OnChestOpened.findPlayer( data ).orElse( null ) ) );

			handler.getConfig()
				.define( "any_chest_spawn_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
