package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnAnimalTamed;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.TamingStrongerAnimals;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class CertificateOfTaming extends AccessoryHandler {
	public CertificateOfTaming() {
		super( MajruszsAccessories.CERTIFICATE_OF_TAMING, CertificateOfTaming.class );

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
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.tamer ) );

			handler.getConfig()
				.define( "taming_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnCertificate( OnAnimalTamed data ) {
			this.spawnFlyingItem( data.getLevel(), data.animal.position(), data.tamer.position() );
		}
	}
}
