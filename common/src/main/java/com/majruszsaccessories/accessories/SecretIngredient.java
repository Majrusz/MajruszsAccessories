package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.StrongerPotions;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemBrewed;
import com.mlib.data.Serializable;
import com.mlib.level.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;

@AutoInstance
public class SecretIngredient extends AccessoryHandler {
	public SecretIngredient() {
		super( MajruszsAccessories.SECRET_INGREDIENT );

		this.add( StrongerPotions.create( 0.6f, 1 ) )
			.add( BrewingDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class BrewingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.0334f;

		public static ISupplier< AccessoryItem > create() {
			return BrewingDropChance::new;
		}

		protected BrewingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnItemBrewed.listen( this::spawnAccessory )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->LevelHelper.getNearestPlayer( data.getLevel(), data.blockPos, 30.0f ) ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "brewing_drop_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}

		private void spawnAccessory( OnItemBrewed data ) {
			AnyPos pos = AnyPos.from( data.blockPos ).center();

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
