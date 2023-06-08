package com.majruszsaccessories.recipes;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.BoosterItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class BoostAccessoriesRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleRecipeSerializer<>( BoostAccessoriesRecipe::new );
	}

	public BoostAccessoriesRecipe( ResourceLocation id ) {
		super( id );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return data.getAccessoriesSize() == 1 && data.getBoostersSize() == 1;
	}

	@Override
	public ItemStack assemble( CraftingContainer container ) {
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
		return Registries.BOOST_ACCESSORIES_RECIPE.get();
	}
}
