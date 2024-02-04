package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.majruszsaccessories.accessories.components.HarvestingDoubleCrops;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.OnAccessoryDropChanceGet;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.world.level.block.Blocks;

@AutoInstance
public class TamedPotatoBeetle extends AccessoryHandler {
	public TamedPotatoBeetle() {
		super( MajruszsAccessories.TAMED_POTATO_BEETLE, TamedPotatoBeetle.class );

		this.add( HarvestingDoubleCrops.create( 0.25f ) )
			.add( HarvestingDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.NATURE_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class HarvestingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.0025f;
		float potatoMultiplier = 2.0f;

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

					return Events.dispatch( new OnAccessoryDropChanceGet( chance, data.entity ) ).check();
				} );

			handler.getConfig()
				.define( "harvesting_drop", subconfig->{
					subconfig.define( "harvesting_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
					subconfig.define( "potato_multiplier", Reader.number(), s->this.potatoMultiplier, ( s, v )->this.potatoMultiplier = Range.of( 1.0f, 10.0f )
						.clamp( v ) );
				} );
		}
	}
}
