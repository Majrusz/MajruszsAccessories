package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.mlib.annotation.AutoInstance;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

@AutoInstance
public class AdventurerGuide extends AccessoryHandler {
	public AdventurerGuide() {
		super( MajruszsAccessories.ADVENTURER_GUIDE );

		this.add( MoreChestLoot.create( 1.2f ) )
			.add( AddToAllChests.create() );
	}

	static class AddToAllChests extends AccessoryComponent {
		float chance = 0.025f;

		public static ISupplier< AccessoryItem > create() {
			return AddToAllChests::new;
		}

		protected AddToAllChests( Handler< AccessoryItem > handler ) {
			super( handler );

			MoreChestLoot.OnChestOpened.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "any_chest_spawn_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}
	}
}
