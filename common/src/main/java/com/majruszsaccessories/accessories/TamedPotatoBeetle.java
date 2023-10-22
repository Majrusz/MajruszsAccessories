package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.DoubleCrops;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.contexts.OnAccessoryDropChanceGet;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.base.Contexts;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import net.minecraft.world.level.block.Blocks;

@AutoInstance
public class TamedPotatoBeetle extends AccessoryHandler {
	public TamedPotatoBeetle() {
		super( MajruszsAccessories.TAMED_POTATO_BEETLE );

		this.add( DoubleCrops.create( 0.25f ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		float chance = 0.002f;
		float potatoMultiplier = 2.5f;

		public static ISupplier< AccessoryItem > create() {
			return DropChance::new;
		}

		protected DropChance( Handler< AccessoryItem > handler ) {
			super( handler );

			DoubleCrops.OnCropHarvested.listen( this::addToGeneratedLoot )
				.addCondition( data->{
					float chance = this.chance;
					if( data.blockState != null && data.blockState.getBlock().equals( Blocks.POTATOES ) ) {
						chance *= this.potatoMultiplier;
					}

					return Contexts.dispatch( new OnAccessoryDropChanceGet( chance, data.entity ) ).check();
				} );

			Serializable config = handler.getConfig();
			config.defineCustom( "harvesting_drop", subconfig->{
				subconfig.defineFloat( "chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
				subconfig.defineFloat( "potato_multiplier", ()->this.potatoMultiplier, x->this.potatoMultiplier = Range.of( 1.0f, 10.0f ).clamp( x ) );
			} );
		}
	}
}
