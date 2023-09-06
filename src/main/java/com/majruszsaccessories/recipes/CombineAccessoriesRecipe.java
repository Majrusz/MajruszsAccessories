package com.majruszsaccessories.recipes;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.Registries;
import com.mlib.math.Range;
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

import static com.majruszsaccessories.accessories.AccessoryHolder.BONUS_RANGE;

public class CombineAccessoriesRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleCraftingRecipeSerializer<>( CombineAccessoriesRecipe::new );
	}

	public CombineAccessoriesRecipe( ResourceLocation id, CraftingBookCategory category ) {
		super( id, category );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return data.getAccessoriesSize() > 1
			&& data.hasIdenticalItemTypes()
			&& data.getMaxBonus() < AccessoryHolder.BONUS_RANGE.to
			&& data.getBoostersSize() == 0;
	}

	@Override
	public ItemStack assemble( CraftingContainer container, RegistryAccess registryAccess ) {
		RecipeData data = RecipeData.build( container );
		float craftingMaxBonus = data.getMaxBonus();
		float minBonus = BONUS_RANGE.clamp( craftingMaxBonus - 0.02f * ( data.getAccessoriesSize() - 1 ) );
		float maxBonus = BONUS_RANGE.clamp( craftingMaxBonus + 0.07f * ( data.getAccessoriesSize() - 1 ) );

		return AccessoryHolder.create( data.getAccessory( 0 ).getItem() ).setBonus( new Range<>( minBonus, maxBonus ) ).getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return Registries.COMBINE_ACCESSORIES_RECIPE.get();
	}
}
