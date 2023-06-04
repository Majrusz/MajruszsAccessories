package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.boosters.BoosterItem;
import com.mlib.Random;
import com.mlib.gamemodifiers.Condition;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomConditions {
	public static < DataType > Condition< DataType > hasAccessory( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->entity.apply( data ) != null && AccessoryHolder.hasAccessory( entity.apply( data ), item.get() ) );
	}

	public static < DataType > Condition< DataType > hasBooster( Supplier< BoosterItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->entity.apply( data ) != null && AccessoryHolder.hasBooster( entity.apply( data ), item.get() ) );
	}

	public static < DataType > Condition< DataType > chance( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity,
		Function< AccessoryHolder, Float > chance
	) {
		return Condition.predicate( data->{
			AccessoryHolder holder = AccessoryHolder.find( entity.apply( data ), item.get() );

			return holder.isValid() && Random.tryChance( chance.apply( holder ) );
		} );
	}
}
