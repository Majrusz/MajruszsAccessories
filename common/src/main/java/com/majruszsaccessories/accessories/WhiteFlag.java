package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.ReduceDamage;
import com.majruszsaccessories.common.Handler;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnPlayerInteracted;
import com.mlib.time.TimeHelper;

@AutoInstance
public class WhiteFlag extends AccessoryHandler {
	public WhiteFlag() {
		super( MajruszsAccessories.WHITE_FLAG );

		this.add( ReduceDamage.create( 0.2f ) )
			.add( SwingBehavior.create() );
	}

	static class SwingBehavior extends AccessoryComponent {
		public static ISupplier< AccessoryItem > create() {
			return SwingBehavior::new;
		}

		protected SwingBehavior( Handler< AccessoryItem > handler ) {
			super( handler );

			OnPlayerInteracted.listen( this::swing )
				.addCondition( data->data.itemStack.is( this.getItem() ) )
				.addCondition( data->!data.player.getCooldowns().isOnCooldown( this.getItem() ) );
		}

		private void swing( OnPlayerInteracted data ) {
			data.player.swing( data.hand );
			data.player.getCooldowns().addCooldown( data.itemStack.getItem(), TimeHelper.toTicks( 0.5 ) );
		}
	}
}
