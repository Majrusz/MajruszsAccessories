package com.majruszsaccessories.recipes;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.math.Range;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public record RecipeData( AccessoryItem item, List< Float > bonuses ) {
	public RecipeData( AccessoryItem item, List< Float > bonuses ) {
		this.item = item;
		this.bonuses = bonuses;

		Collections.sort( this.bonuses );
	}

	ItemStack build( float minBonus, float maxBonus ) {
		if( ( maxBonus - minBonus ) < 1e-5f ) {
			return AccessoryHolder.create( this.item, minBonus ).getItemStack();
		} else {
			return AccessoryHolder.create( this.item, new Range<>( minBonus, maxBonus ) ).getItemStack();
		}
	}

	Item getItem() {
		return this.item;
	}

	List< Float > getBonuses() {
		return this.bonuses;
	}

	int getBonusesSize() {
		return this.bonuses.size();
	}

	float getAverageBonus() {
		return this.bonuses.stream().reduce( 0.0f, Float::sum ) / this.bonuses.size();
	}

	float getStandardDeviation() {
		float average = this.getAverageBonus();

		return ( float )Math.sqrt( this.bonuses.stream()
			.reduce( 0.0f, ( sum, bonus )->sum + ( float )Math.pow( bonus - average, 2.0 ) ) / this.bonuses.size() );
	}

	float getMaxBonus() {
		return this.bonuses.get( this.bonuses.size() - 1 );
	}

	float getMinBonus() {
		return this.bonuses.get( 0 );
	}
}
