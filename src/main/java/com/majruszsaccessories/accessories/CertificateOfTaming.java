package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.AccessoryComponent;
import com.majruszsaccessories.components.EnhanceTamedAnimal;
import com.majruszsaccessories.components.TradeOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnAnimalTame;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class CertificateOfTaming extends AccessoryBase {
	public CertificateOfTaming() {
		super( Registries.CERTIFICATE_OF_TAMING );

		this.name( "CertificateOfTaming" )
			.add( EnhanceTamedAnimal.create() )
			.add( TradeOffer.create( VillagerProfession.SHEPHERD, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static AccessoryComponent.ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.02, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Certificate of Taming to drop when taming animals." );

			OnAnimalTame.listen( this::spawnCertificate )
				.addCondition( Condition.chance( chance ) )
				.insertTo( group );
		}

		private void spawnCertificate( OnAnimalTame.Data data ) {
			this.spawnFlyingItem( data.getLevel(), data.animal.position(), data.tamer.position() );
		}
	}
}
