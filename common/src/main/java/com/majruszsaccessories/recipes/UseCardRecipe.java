package com.majruszsaccessories.recipes;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class UseCardRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleCraftingRecipeSerializer<>( UseCardRecipe::new );
	}

	public UseCardRecipe( ResourceLocation id, CraftingBookCategory category ) {
		super( id, category );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return data.getAccessoriesSize() == 1
			&& data.getBoostersSize() == 0
			&& data.getCardsSize() == 1;
	}

	@Override
	public ItemStack assemble( CraftingContainer container, RegistryAccess registryAccess ) {
		RecipeData data = RecipeData.build( container );
		AccessoryHolder holder = data.getAccessory( 0 ).copy();
		data.getCard( 0 ).apply( holder );

		return holder.getItemStack();
	}

	@Override
	public NonNullList< ItemStack > getRemainingItems( CraftingContainer container ) {
		RecipeData data = RecipeData.build( container );
		NonNullList< ItemStack > remainingItems = NonNullList.withSize( container.getContainerSize(), ItemStack.EMPTY );
		List< ItemStack > itemStacks = data.getCard( 0 ).getCraftingRemainder( data.getAccessory( 0 ) );
		for( int idx = 0; idx < itemStacks.size(); ++idx ) {
			remainingItems.set( idx, itemStacks.get( idx ) );
		}

		return remainingItems;
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.USE_CARD_RECIPE.get();
	}
}
