package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnItemBrewed;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.StrongerPotions;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class SecretIngredient extends AccessoryHandler {
	public SecretIngredient() {
		super( MajruszsAccessories.SECRET_INGREDIENT, SecretIngredient.class );

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
				.addCondition( CustomConditions.dropChance( s->this.chance, data->LevelHelper.getNearestPlayer( data.getLevel(), data.blockPos, 30.0f ) ) );

			handler.getConfig()
				.define( "brewing_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnItemBrewed data ) {
			AnyPos pos = AnyPos.from( data.blockPos ).center();

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
