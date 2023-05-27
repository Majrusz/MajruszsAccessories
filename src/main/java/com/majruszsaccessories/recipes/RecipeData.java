package com.majruszsaccessories.recipes;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.math.Range;
import net.minecraft.util.Mth;
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
		return AccessoryHolder.create( this.item, new Range<>( minBonus, maxBonus ) ).getItemStack();
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

	float getMaxBonus() {
		return this.bonuses.get( this.bonuses.size() - 1 );
	}

	float getMinBonus() {
		return this.bonuses.get( 0 );
	}

	float determineRatio() {
		float min = this.getMinBonus(), max = this.getMaxBonus();
		if( min == max )
			return 1.0f;

		float average = this.getAverageBonus();
		float std = ( float )Math.sqrt( this.bonuses.stream()
			.reduce( 0.0f, ( sum, bonus )->sum + ( float )Math.pow( bonus - average, 2.0f ) ) / this.bonuses.size() );
		return Mth.clamp( 1.0f - 2.0f * std / ( AccessoryHolder.BONUS_RANGE.to - AccessoryHolder.BONUS_RANGE.from ), 0.0f, 1.0f );
	}
}
