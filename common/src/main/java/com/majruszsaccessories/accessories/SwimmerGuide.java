package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.level.BlockHelper;
import com.mlib.math.Range;
import net.minecraft.world.level.material.Fluids;

@AutoInstance
public class SwimmerGuide extends AccessoryHandler {
	public SwimmerGuide() {
		super( MajruszsAccessories.SWIMMER_GUIDE );

		this.add( SwimmingSpeedBonus.create( 0.2f ) )
			.add( UnderwaterChestDropChance.create() );
	}

	static class UnderwaterChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.05f;

		public static ISupplier< AccessoryItem > create() {
			return UnderwaterChestDropChance::new;
		}

		protected UnderwaterChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.hasLevel() )
				.addCondition( data->data.origin != null )
				.addCondition( data->BlockHelper.getState( data.getLevel(), data.origin ).getFluidState().isSourceOfType( Fluids.WATER ) )
				.addCondition( data->data.lootId.toString().contains( "chest" ) )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "underwater_chest_spawn_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}
	}
}
