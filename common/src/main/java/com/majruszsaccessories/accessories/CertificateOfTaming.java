package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.TamingStrongerAnimals;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnAnimalTamed;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class CertificateOfTaming extends AccessoryHandler {
	public CertificateOfTaming() {
		super( MajruszsAccessories.CERTIFICATE_OF_TAMING );

		this.add( TamingStrongerAnimals.create( 0.2f ) )
			.add( TamingDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class TamingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.1f;

		public static ISupplier< AccessoryItem > create() {
			return TamingDropChance::new;
		}

		protected TamingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnAnimalTamed.listen( this::spawnCertificate )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.tamer ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "taming_drop_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}

		private void spawnCertificate( OnAnimalTamed data ) {
			this.spawnFlyingItem( data.getLevel(), data.animal.position(), data.tamer.position() );
		}
	}
}
