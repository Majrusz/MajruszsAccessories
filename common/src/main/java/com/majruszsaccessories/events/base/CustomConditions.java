package com.majruszsaccessories.events.base;

import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Events;
import com.majruszsaccessories.events.OnAccessoryDropChanceGet;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomConditions {
	public static < DataType > Condition< DataType > dropChance( Function< DataType, Float > chance, Function< DataType, Entity > entity ) {
		return Condition.predicate( data->Events.dispatch( new OnAccessoryDropChanceGet( chance.apply( data ), entity.apply( data ) ) ).check() );
	}

	public static < DataType > Condition< DataType > dropChance( Supplier< Float > chance, Function< DataType, Entity > entity ) {
		return CustomConditions.dropChance( data->chance.get(), entity );
	}
}
