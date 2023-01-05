package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.EnhanceTamedAnimal;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnAnimalTame;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class CertificateOfTamingItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "certificate_of_taming" );

	public CertificateOfTamingItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "CertificateOfTaming" );

			new EnhanceTamedAnimal( Registries.CERTIFICATE_OF_TAMING, ID );
			new AddDropChance( Registries.CERTIFICATE_OF_TAMING, ID );
			new TradeOffer( Registries.CERTIFICATE_OF_TAMING, ID );
		}
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new OnAnimalTame.Context( this::spawnCertificate )
				.addCondition( new DropChance( 0.01 ) )
				.insertTo( this );
		}

		private void spawnCertificate( OnAnimalTame.Data data ) {
			this.spawnFlyingItem( data.level, data.animal.position(), data.tamer.position() );
		}

		static class DropChance extends Condition.Chance< OnAnimalTame.Data > {
			public DropChance( double chance ) {
				super( chance );

				this.chance.name( "drop_chance" ).comment( "Chance for Certificate of Taming to drop when taming animals." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.SHEPHERD, 5 );
		}
	}
}
