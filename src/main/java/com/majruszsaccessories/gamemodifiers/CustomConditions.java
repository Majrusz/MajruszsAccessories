package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryDropChance;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Priority;
import net.minecraft.world.entity.Entity;
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

	public static < DataType > Condition< DataType > dropChance( DoubleConfig chance, Function< DataType, Entity > entity ) {
		return Condition.< DataType > predicate( data->Random.tryChance( OnAccessoryDropChance.dispatch( chance.getOrDefault(), entity.apply( data ) )
				.getChance() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( chance );
	}
}
