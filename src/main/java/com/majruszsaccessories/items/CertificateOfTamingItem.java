package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.EnhanceTamedAnimal;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnAnimalTameContext;
import com.mlib.gamemodifiers.data.OnAnimalTameData;
import com.mlib.levels.LevelHelper;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class CertificateOfTamingItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "certificate_of_taming" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "CertificateOfTaming", "" ) );

	public static Supplier< CertificateOfTamingItem > create() {
		GameModifiersHolder< CertificateOfTamingItem > holder = new GameModifiersHolder<>( ID, CertificateOfTamingItem::new );
		holder.addModifier( EnhanceTamedAnimal::new );
		holder.addModifier( AddDropChance::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnAnimalTameContext onLoot = new OnAnimalTameContext( this::spawnCertificate );
			onLoot.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.01, "drop_chance", "Chance for Certificate of Taming to drop when taming animals." ) );

			this.addContext( onLoot );
		}

		private void spawnCertificate( OnAnimalTameData data ) {
			assert data.level != null;
			ItemStack itemStack = AccessoryHandler.construct( this.item.get() );
			LevelHelper.spawnItemEntityFlyingTowardsDirection( itemStack, data.level, data.animal.position(), data.tamer.position().add( 0.0, 2.0, 0.0 ) );
		}
	}
}
