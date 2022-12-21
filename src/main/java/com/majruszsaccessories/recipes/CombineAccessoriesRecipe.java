package com.majruszsaccessories.recipes;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CombineAccessoriesRecipe extends CustomRecipe {
	public static float BONUS_OFFSET = 0.04f;

	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleCraftingRecipeSerializer<>( CombineAccessoriesRecipe::new );
	}

	public CombineAccessoriesRecipe( ResourceLocation id, CraftingBookCategory category ) {
		super( id, category );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = this.buildCraftingData( container );

		return data != null && data.getBonusesSize() > 1;
	}

	@Override
	public ItemStack assemble( CraftingContainer container ) {
		RecipeData data = this.buildCraftingData( container );
		float craftingMaxBonus = data.getMaxBonus();
		float ratio = data.determineRatio();
		float bonusOffset = data.getBonusesSize() * BONUS_OFFSET;
		float minBonus = craftingMaxBonus - ( 1.0f - ratio ) * bonusOffset;
		float maxBonus = craftingMaxBonus + ratio * bonusOffset;

		return data.build( minBonus, maxBonus );
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return Registries.COMBINE_ACCESSORIES_RECIPE.get();
	}

	private RecipeData buildCraftingData( CraftingContainer container ) {
		AccessoryItem accessory = null;
		List< Float > bonuses = new ArrayList<>();
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( itemStack.isEmpty() )
				continue;

			if( itemStack.getItem() instanceof AccessoryItem item ) {
				if( accessory == null ) {
					accessory = item;
				} else if( accessory != item ) {
					return null;
				}
				bonuses.add( new AccessoryHandler( itemStack ).getBonus() );
			} else {
				return null;
			}
		}

		RecipeData data = new RecipeData( accessory, bonuses );
		return accessory != null && data.getMaxBonus() < AccessoryHandler.MAX_BONUS ? data : null;
	}
}
