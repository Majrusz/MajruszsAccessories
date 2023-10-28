package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.HarvestingDoubleCrops;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.OnAccessoryDropChanceGet;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.base.Contexts;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import net.minecraft.world.level.block.Blocks;

@AutoInstance
public class TamedPotatoBeetle extends AccessoryHandler {
	public TamedPotatoBeetle() {
		super( MajruszsAccessories.TAMED_POTATO_BEETLE );

		this.add( HarvestingDoubleCrops.create( 0.25f ) )
			.add( HarvestingDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class HarvestingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.002f;
		float potatoMultiplier = 2.5f;

		public static ISupplier< AccessoryItem > create() {
			return HarvestingDropChance::new;
		}

		protected HarvestingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			HarvestingDoubleCrops.OnCropHarvested.listen( this::addToGeneratedLoot )
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
