package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AccessoryHolders {
	public static final AccessoryHolders EMPTY = new AccessoryHolders( List.of() );
	final List< AccessoryHolder > holders;

	public static AccessoryHolders get( @Nullable LivingEntity entity ) {
		return entity != null ? ( ( IMixinLivingEntity )entity ).majruszsaccessories$getAccessoryHolders() : EMPTY;
	}

	public static AccessoryHolders find( LivingEntity entity ) {
		Predicate< ItemStack > predicate = itemStack->itemStack.getItem() instanceof AccessoryItem;
		if( MajruszsAccessories.SLOT_INTEGRATION.isInstalled() ) {
			return new AccessoryHolders( MajruszsAccessories.SLOT_INTEGRATION.find( entity, predicate ) );
		} else {
			ItemStack itemStack = entity.getOffhandItem();
			if( predicate.test( itemStack ) ) {
				return new AccessoryHolders( List.of( itemStack ) );
			}

			return AccessoryHolders.EMPTY;
		}
	}

	public AccessoryHolder get( Supplier< AccessoryItem > item ) {
		for( AccessoryHolder holder : this.holders ) {
			if( holder.is( item.get() ) ) {
				return holder;
			}
		}

		return AccessoryHolder.EMPTY;
	}

	public int getBoostersCount( Supplier< BoosterItem > item ) {
		return ( int )this.holders.stream().filter( holder->holder.has( item.get() ) ).count();
	}

	private AccessoryHolders( List< ItemStack > itemStacks ) {
		this.holders = itemStacks.stream().map( AccessoryHolder::getOrCreate ).toList();
	}
}
