package com.majruszsaccessories.recipes;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.items.CardItem;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record RecipeData( List< AccessoryHolder > accessories, List< BoosterItem > boosters, List< CardItem > cards ) {
	public static RecipeData build( CraftingContainer container ) {
		RecipeData data = new RecipeData();
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( itemStack.isEmpty() ) {
				continue;
			}

			if( itemStack.getItem() instanceof AccessoryItem ) {
				data.accessories.add( AccessoryHolder.getOrCreate( itemStack ) );
			} else if( itemStack.getItem() instanceof BoosterItem booster ) {
				data.boosters.add( booster );
			} else if( itemStack.getItem() instanceof CardItem card ) {
				data.cards.add( card );
			} else {
				return new RecipeData();
			}
		}

		data.accessories.sort( ( left, right )->Float.compare( left.getBaseBonus(), right.getBaseBonus() ) );
		return data;
	}

	public RecipeData() {
		this( new ArrayList<>(), new ArrayList<>(), new ArrayList<>() );
	}

	public AccessoryHolder getAccessory( int idx ) {
		return this.accessories.get( idx );
	}

	public BoosterItem getBooster( int idx ) {
		return this.boosters.get( idx );
	}

	public CardItem getCard( int idx ) {
		return this.cards.get( idx );
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

	int getCardsSize() {
		return this.cards.size();
	}

	boolean hasAccessory( AccessoryItem item ) {
		return this.accessories.stream().anyMatch( holder->holder.getItem().equals( item ) );
	}

	boolean hasIdenticalItemTypes() {
		return this.accessories.stream().allMatch( holder->holder.getItem().equals( this.accessories.get( 0 ).getItem() ) );
	}
}
