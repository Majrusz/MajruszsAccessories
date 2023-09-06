package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryDropChance;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Priority;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomConditions {
	public static < DataType > Condition< DataType > hasAccessory( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->AccessoryHolder.hasAccessory( entity.apply( data ), item.get() ) );
	}

	public static < DataType > Condition< DataType > hasBooster( Supplier< BoosterItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->AccessoryHolder.hasBooster( entity.apply( data ), item.get() ) );
	}

	public static < DataType > Condition< DataType > chance( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity,
		Function< AccessoryHolder, Float > chance
	) {
		return Condition.predicate( data->{
			AccessoryHolder holder = AccessoryHolder.find( entity.apply( data ), item.get() );

			return holder.isValid() && Random.tryChance( chance.apply( holder ) );
		} );
	}

	public static < DataType > Condition< DataType > dropChance( DoubleConfig chance, Function< DataType, Entity > entity ) {
		return Condition.< DataType > predicate( data->OnAccessoryDropChance.dispatch( chance.getOrDefault(), entity.apply( data ) ).tryChance() )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( chance );
	}
}
