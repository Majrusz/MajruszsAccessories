package com.majruszsaccessories.contexts.base;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.contexts.OnAccessoryDropChanceGet;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Contexts;
import com.mlib.math.Random;
import com.mlib.platform.LogicalSafe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomConditions {
	private static final LogicalSafe< AccessoryHolder > HOLDER = LogicalSafe.of( ()->AccessoryHolder.create( ItemStack.EMPTY ) );

	public static < DataType > Condition< DataType > hasAccessory( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->CustomConditions.updateHolder( entity.apply( data ), item.get() ).isValid() );
	}

	public static < DataType > Condition< DataType > hasBooster( Supplier< BoosterItem > item, Function< DataType, LivingEntity > entity ) {
		return Condition.predicate( data->CustomConditions.updateHolder( entity.apply( data ), item.get() ).isValid() );
	}

	public static < DataType > Condition< DataType > chance( Supplier< AccessoryItem > item, Function< DataType, LivingEntity > entity,
		Function< AccessoryHolder, Float > chance
	) {
		return Condition.predicate( data->{
			AccessoryHolder holder = CustomConditions.updateHolder( entity.apply( data ), item.get() );

			return holder.isValid() && Random.check( chance.apply( holder ) );
		} );
	}

	public static < DataType > Condition< DataType > dropChance( Supplier< Float > chance, Function< DataType, Entity > entity ) {
		return Condition.predicate( data->Contexts.dispatch( new OnAccessoryDropChanceGet( chance.get(), entity.apply( data ) ) ).check() );
	}

	public static AccessoryHolder getLastHolder() {
		return HOLDER.get();
	}

	private static AccessoryHolder updateHolder( LivingEntity entity, AccessoryItem item ) {
		if( entity != null ) {
			AccessoryHolder holder = AccessoryHolder.find( entity, item );
			HOLDER.set( holder );

			return holder;
		}

		return AccessoryHolder.create( ItemStack.EMPTY );
	}

	private static AccessoryHolder updateHolder( LivingEntity entity, BoosterItem item ) {
		if( entity != null ) {
			AccessoryHolder holder = AccessoryHolder.find( entity, item );
			HOLDER.set( holder );

			return holder;
		}

		return AccessoryHolder.create( ItemStack.EMPTY );
	}
}
