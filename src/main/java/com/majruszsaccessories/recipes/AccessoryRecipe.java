package com.majruszsaccessories.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class AccessoryRecipe extends CustomRecipe {
	public static float BONUS_OFFSET = 0.05f;
	final AccessoryItem result;
	final List< AccessoryItem > ingredients;
	public static Supplier< RecipeSerializer< ? > > create() {
		return AccessoryRecipe.Serializer::new;
	}

	public AccessoryRecipe( ResourceLocation id, AccessoryItem result, List< AccessoryItem > ingredients ) {
		super( id, CraftingBookCategory.EQUIPMENT );
		this.result = result;
		this.ingredients = ingredients;
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		List< AccessoryItem > ingredients = new ArrayList<>( this.ingredients );
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( itemStack.isEmpty() )
				continue;

			boolean foundMatch = false;
			Iterator< AccessoryItem > iterator = ingredients.iterator();
			while( iterator.hasNext() ) {
				if( itemStack.getItem() == iterator.next() ) {
					iterator.remove();
					foundMatch = true;
					break;
				}
			}
			if( !foundMatch ) {
				return false;
			}
		}

		return ingredients.isEmpty();
	}

	@Override
	public ItemStack assemble( CraftingContainer container ) {
		List< Float > bonuses = new ArrayList<>();
		for( int i = 0; i < container.getContainerSize(); ++i ) {
			ItemStack itemStack = container.getItem( i );
			if( !itemStack.isEmpty() ) {
				bonuses.add( new AccessoryHandler( itemStack ).getBonus() );
			}
		}
		RecipeData data = new RecipeData( this.result, bonuses );
		float average = data.getAverageBonus();

		return data.build( average - BONUS_OFFSET, average + BONUS_OFFSET );
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return Registries.ACCESSORY_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer< AccessoryRecipe > {
		public AccessoryRecipe fromJson( ResourceLocation id, JsonObject object ) {
			AccessoryItem result = ( AccessoryItem )GsonHelper.getAsItem( object, "result" );
			List< AccessoryItem > ingredients = serializeIngredients( GsonHelper.getAsJsonArray( object, "ingredients" ) );
			if( ingredients.isEmpty() ) {
				throw new JsonParseException( "No ingredients for accessory recipe" );
			}

			return new AccessoryRecipe( id, result, ingredients );
		}

		public AccessoryRecipe fromNetwork( ResourceLocation id, FriendlyByteBuf buffer ) {
			int size = buffer.readVarInt();
			List< AccessoryItem > ingredients = new ArrayList<>();
			for( int idx = 0; idx < size; ++idx ) {
				ingredients.add( ( AccessoryItem )buffer.readItem().getItem() );
			}

			AccessoryItem result = ( AccessoryItem )buffer.readItem().getItem();
			return new AccessoryRecipe( id, result, ingredients );
		}

		public void toNetwork( FriendlyByteBuf buffer, AccessoryRecipe recipe ) {
			buffer.writeVarInt( recipe.ingredients.size() );
			recipe.ingredients.forEach( ingredient->buffer.writeItem( new ItemStack( ingredient ) ) );
			buffer.writeItem( new ItemStack( recipe.result ) );
		}

		private static List< AccessoryItem > serializeIngredients( JsonArray array ) {
			List< AccessoryItem > ingredients = new ArrayList<>();
			for( int i = 0; i < array.size(); ++i ) {
				ingredients.add( ( AccessoryItem )GsonHelper.convertToItem( array.get( i ), "item" ) );
			}

			return ingredients;
		}
	}
}
