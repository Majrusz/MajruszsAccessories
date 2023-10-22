package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.TamingStrongerAnimals;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnAnimalTamed;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class CertificateOfTaming extends AccessoryHandler {
	public CertificateOfTaming() {
		super( MajruszsAccessories.CERTIFICATE_OF_TAMING );

		this.add( TamingStrongerAnimals.create( 0.2f ) )
			.add( TamingDropChance.create() );
	}

	static class TamingDropChance extends AccessoryComponent {
		float chance = 0.1f;

		public static ISupplier< AccessoryItem > create() {
			return TamingDropChance::new;
		}

		protected TamingDropChance( Handler< AccessoryItem > handler ) {
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
