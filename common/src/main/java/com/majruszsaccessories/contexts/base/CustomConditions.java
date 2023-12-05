package com.majruszsaccessories.contexts.base;

import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.math.Random;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.contexts.OnAccessoryDropChanceGet;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomConditions {
	public static < DataType > Condition< DataType > hasAccessory( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->AccessoryHolder.get( entity.apply( data ) ).is( item.get() ) );
	}

	public static < DataType > Condition< DataType > hasBooster( Supplier< BoosterItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->AccessoryHolder.get( entity.apply( data ) ).has( item.get() ) );
	}

	public static < DataType > Condition< DataType > chance( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity,
		Function< AccessoryHolder, Float > chance
	) {
		return Condition.predicate( data->{
			AccessoryHolder holder = AccessoryHolder.get( entity.apply( data ) );

			return holder.is( item.get() ) && Random.check( chance.apply( holder ) );
		} );
	}

	public static < DataType > Condition< DataType > dropChance( Function< DataType, Float > chance, Function< DataType, Entity > entity ) {
		return Condition.predicate( data->Events.dispatch( new OnAccessoryDropChanceGet( chance.apply( data ), entity.apply( data ) ) ).check() );
	}

	public static < DataType > Condition< DataType > dropChance( Supplier< Float > chance, Function< DataType, Entity > entity ) {
		return CustomConditions.dropChance( data->chance.get(), entity );
	}
}
