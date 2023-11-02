package com.majruszsaccessories.recipes;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.mlib.math.Range;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class CombineAccessoriesRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleCraftingRecipeSerializer<>( CombineAccessoriesRecipe::new );
	}

	public CombineAccessoriesRecipe( CraftingBookCategory category ) {
		super( category );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return data.getAccessoriesSize() > 1
			&& data.hasIdenticalItemTypes()
			&& data.getMaxBonus() < MajruszsAccessories.CONFIG.efficiency.range.to
			&& data.getBoostersSize() == 0;
	}

	@Override
	public ItemStack assemble( CraftingContainer container, RegistryAccess registryAccess ) {
		RecipeData data = RecipeData.build( container );
		float craftingMaxBonus = data.getMaxBonus();
		float minBonus = MajruszsAccessories.CONFIG.efficiency.range.clamp( craftingMaxBonus - 0.02f * ( data.getAccessoriesSize() - 1 ) );
		float maxBonus = MajruszsAccessories.CONFIG.efficiency.range.clamp( craftingMaxBonus + 0.07f * ( data.getAccessoriesSize() - 1 ) );

		return AccessoryHolder.create( data.getAccessory( 0 ).getItem() ).setBonus( Range.of( minBonus, maxBonus ) ).getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.COMBINE_ACCESSORIES_RECIPE.get();
	}
}
