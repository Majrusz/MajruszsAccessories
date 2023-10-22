package com.majruszsaccessories.recipes;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.boosters.BoosterItem;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record RecipeData( List< AccessoryHolder > accessories, List< BoosterItem > boosters ) {
	public static RecipeData build( CraftingContainer container ) {
		RecipeData data = new RecipeData();
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( itemStack.isEmpty() ) {
				continue;
			}

			if( itemStack.getItem() instanceof AccessoryItem ) {
				data.accessories.add( AccessoryHolder.create( itemStack ) );
			} else if( itemStack.getItem() instanceof BoosterItem item ) {
				data.boosters.add( item );
			} else {
				return new RecipeData();
			}
		}

		data.accessories.sort( ( left, right )->Float.compare( left.getBaseBonus(), right.getBaseBonus() ) );
		return data;
	}

	public RecipeData() {
		this( new ArrayList<>(), new ArrayList<>() );
	}

	public AccessoryHolder getAccessory( int idx ) {
		return this.accessories.get( idx );
	}

	public BoosterItem getBooster( int idx ) {
		return this.boosters.get( idx );
	}

	float getStandardDeviation() {
		float average = this.getAverageBonus();
		double variation = this.accessories.stream()
			.map( AccessoryHolder::getBaseBonus )
			.reduce( 0.0f, ( sum, bonus )->sum + ( float )Math.pow( bonus - average, 2.0 ) ) / this.accessories.size();

		return ( float )Math.sqrt( variation );
	}

	float getMaxBonus() {
		return this.accessories.get( this.accessories.size() - 1 ).getBaseBonus();
	}

	float getMinBonus() {
		return this.accessories.get( 0 ).getBaseBonus();
	}

	float getAverageBonus() {
		return this.accessories.stream().map( AccessoryHolder::getBaseBonus ).reduce( 0.0f, Float::sum ) / this.accessories.size();
	}

	int getAccessoriesSize() {
		return this.accessories.size();
	}

	int getBoostersSize() {
		return this.boosters.size();
	}

	boolean hasAccessory( AccessoryItem item ) {
		return this.accessories.stream().anyMatch( holder->holder.getItem().equals( item ) );
	}

	boolean hasIdenticalItemTypes() {
		return this.accessories.stream().allMatch( holder->holder.getItem().equals( this.accessories.get( 0 ).getItem() ) );
	}

}
