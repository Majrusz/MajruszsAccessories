package com.majruszsaccessories.recipes;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.boosters.BoosterItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class BoostAccessoriesRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleCraftingRecipeSerializer<>( BoostAccessoriesRecipe::new );
	}

	public BoostAccessoriesRecipe( ResourceLocation id, CraftingBookCategory category ) {
		super( id, category );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return data.getAccessoriesSize() == 1 && data.getBoostersSize() == 1;
	}

	@Override
	public ItemStack assemble( CraftingContainer container, RegistryAccess registryAccess ) {
		RecipeData data = RecipeData.build( container );
		AccessoryHolder holder = data.getAccessory( 0 );
		BoosterItem booster = data.getBooster( 0 );

		return holder.copy().setBooster( booster ).getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.BOOST_ACCESSORIES_RECIPE.get();
	}
}
