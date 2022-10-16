package com.majruszsaccessories.recipes;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class CombineAccessoriesRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleRecipeSerializer<>( CombineAccessoriesRecipe::new );
	}

	public CombineAccessoriesRecipe( ResourceLocation id ) {
		super( id );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		Data data = this.buildCraftingData( container );

		return data != null && data.count > 1;
	}

	@Override
	public ItemStack assemble( CraftingContainer container ) {
		Data data = this.buildCraftingData( container );
		AccessoryHandler handler = AccessoryHandler.setup( new ItemStack( data.item ), 0.2f, 0.3f );

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
		int counter = 0;
		float maxBonus = AccessoryHandler.MIN_BONUS;
		AccessoryItem accessory = null;
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( itemStack.isEmpty() )
				continue;

			if( itemStack.getItem() instanceof AccessoryItem item ) {
				accessory = accessory != null ? accessory : item;
				maxBonus = Math.max( maxBonus, new AccessoryHandler( itemStack ).getBonus() );
				++counter;
			} else {
				return null;
			}
		}

		boolean isValidCraftingRecipe = maxBonus < AccessoryHandler.MAX_BONUS && accessory != null;
		return isValidCraftingRecipe ? new Data( accessory, maxBonus, counter ) : null;
	}

	record Data( AccessoryItem item, float maxBonus, int count ) {}
}
