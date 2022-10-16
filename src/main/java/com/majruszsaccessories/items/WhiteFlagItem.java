package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.mlib.Utility;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnPlayerInteract;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class WhiteFlagItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "white_flag" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "WhiteFlag", "" ) );

	public static Supplier< WhiteFlagItem > create() {
		GameModifiersHolder< WhiteFlagItem > holder = AccessoryItem.newHolder( ID, WhiteFlagItem::new );
		holder.addModifier( SwingBehavior::new );

		return holder::getRegistry;
	}

	static class SwingBehavior extends AccessoryModifier {
		public SwingBehavior( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnPlayerInteract.Context onPlayerInteract = new OnPlayerInteract.Context( this::swing );
			onPlayerInteract.addCondition( data->data.itemStack.getItem() instanceof WhiteFlagItem )
				.addCondition( data->!data.player.getCooldowns().isOnCooldown( data.itemStack.getItem() ) );

			this.addContext( onPlayerInteract );
		}

		private void swing( OnPlayerInteract.Data data ) {
			data.player.swing( data.hand );
			data.player.getCooldowns().addCooldown( data.itemStack.getItem(), Utility.secondsToTicks( 0.5 ) );
		}
	}
}
