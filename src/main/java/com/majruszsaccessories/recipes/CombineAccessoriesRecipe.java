package com.majruszsaccessories.recipes;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CombineAccessoriesRecipe extends CustomRecipe {
	public static float BONUS_OFFSET = 0.04f;

	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleRecipeSerializer<>( CombineAccessoriesRecipe::new );
	}

	public CombineAccessoriesRecipe( ResourceLocation id ) {
		super( id );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		Data data = this.buildCraftingData( container );

		return data != null && data.bonuses.size() > 1;
	}

	@Override
	public ItemStack assemble( CraftingContainer container ) {
		Data data = this.buildCraftingData( container );
		float craftingMaxBonus = data.getMaxBonus();
		float ratio = data.determineRatio();
		float bonusOffset = data.bonuses.size() * BONUS_OFFSET;
		float minBonus = craftingMaxBonus - ( 1.0f - ratio ) * bonusOffset;
		float maxBonus = craftingMaxBonus + ratio * bonusOffset;
		AccessoryHandler handler = AccessoryHandler.setup( new ItemStack( data.item ), minBonus, maxBonus );

		return handler.getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return Registries.COMBINE_ACCESSORIES_RECIPE.get();
	}

	private Data buildCraftingData( CraftingContainer container ) {
		AccessoryItem accessory = null;
		List< Float > bonuses = new ArrayList<>();
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( itemStack.isEmpty() )
				continue;

			if( itemStack.getItem() instanceof AccessoryItem item ) {
				accessory = accessory != null ? accessory : item;
				bonuses.add( new AccessoryHandler( itemStack ).getBonus() );
			} else {
				return null;
			}
		}

		Data data = new Data( accessory, bonuses );
		return accessory != null && data.getMaxBonus() < AccessoryHandler.MAX_BONUS ? data : null;
	}

	public record Data( AccessoryItem item, List< Float > bonuses ) {
		public Data( AccessoryItem item, List< Float > bonuses ) {
			this.item = item;
			this.bonuses = bonuses;

			Collections.sort( this.bonuses );
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

		private float determineRatio() {
			float min = this.getMinBonus(), max = this.getMaxBonus();
			if( min == max )
				return 1.0f;

			float average = this.getAverageBonus();
			float std = ( float )Math.sqrt( this.bonuses.stream().reduce( 0.0f, ( sum, bonus ) -> sum + ( float )Math.pow( bonus - average, 2.0f ) )/this.bonuses.size() );
			return Mth.clamp( 1.0f - 2.0f * std / ( AccessoryHandler.MAX_BONUS - AccessoryHandler.MIN_BONUS ) , 0.0f, 1.0f );
		}
	}
}
