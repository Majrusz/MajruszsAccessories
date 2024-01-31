package com.majruszsaccessories.recipes;

import com.majruszlibrary.events.base.Events;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.events.OnBoosterCompatibilityGet;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BoostAccessoryRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleRecipeSerializer<>( BoostAccessoryRecipe::new );
	}

	public BoostAccessoryRecipe( ResourceLocation id ) {
		super( id );
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );
		if( data.getAccessoriesSize() != 1 ) {
			return false;
		}

		AccessoryHolder holder = data.getAccessory( 0 );
		return data.getCardsSize() == 0
			&& data.getBoostersSize() > 0
			&& data.getBoostersSize() <= holder.getBoosterSlotsLeft()
			&& BoostAccessoryRecipe.areCompatible( data.boosters(), holder.getBoosters() );
	}

	@Override
	public ItemStack assemble( CraftingContainer container ) {
		RecipeData data = RecipeData.build( container );
		AccessoryHolder holder = data.getAccessory( 0 ).copy();
		data.boosters().forEach( holder::addBooster );

		return holder.getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.BOOST_ACCESSORY_RECIPE.get();
	}

	private static boolean areCompatible( List< BoosterItem > a, List< BoosterItem > b ) {
		List< BoosterItem > items = new ArrayList<>( a );
		items.addAll( b );
		for( int i = 0; i < items.size(); ++i ) {
			for( int j = i + 1; j < items.size(); ++j ) {
				if( Events.dispatch( new OnBoosterCompatibilityGet( items.get( i ), items.get( j ) ) ).areIncompatible() ) {
					return false;
				}
			}
		}

		return true;
	}
}
