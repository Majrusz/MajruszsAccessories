package com.majruszsaccessories.recipes;

import com.google.gson.JsonArray;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.math.Range;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AccessoryRecipe extends CustomRecipe {
	final AccessoryItem result;
	final List< AccessoryItem > ingredients;

	public static Supplier< RecipeSerializer< ? > > create() {
		return Serializer::new;
	}

	public AccessoryRecipe( Item result, List< Item > ingredients ) {
		this( ( AccessoryItem )result, ingredients.stream().map( item->( AccessoryItem )item ).toList() );
	}

	public AccessoryRecipe( AccessoryItem result, List< AccessoryItem > ingredients ) {
		super( CraftingBookCategory.EQUIPMENT );

		this.result = result;
		this.ingredients = ingredients;
	}

	@Override
	public boolean matches( CraftingContainer container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return this.ingredients.stream().allMatch( data::hasAccessory );
	}

	@Override
	public ItemStack assemble( CraftingContainer container, RegistryAccess registryAccess ) {
		RecipeData data = RecipeData.build( container );
		float average = data.getAverageBonus();
		float std = data.getStandardDeviation();
		float minBonus = MajruszsAccessories.CONFIG.efficiency.range.clamp( average - std );
		float maxBonus = MajruszsAccessories.CONFIG.efficiency.range.clamp( average + std );

		return AccessoryHolder.create( this.result ).setBonus( Range.of( minBonus, maxBonus ) ).getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.ACCESSORY_RECIPE.get();
	}

	public List< Ingredient > asIngredients() {
		return this.ingredients.stream().map( item->Ingredient.of( new ItemStack( item ) ) ).toList();
	}

	public static class Serializer implements RecipeSerializer< AccessoryRecipe > {
		static final Codec< Item > ACCESSORY = BuiltInRegistries.ITEM.byNameCodec();
		final Codec< AccessoryRecipe > codec;

		public Serializer() {
			this.codec = RecordCodecBuilder.create( builder->{
				return builder.group(
					ACCESSORY.fieldOf( "result" ).forGetter( recipe->recipe.result ),
					ACCESSORY.listOf().fieldOf( "ingredients" ).forGetter( recipe->recipe.ingredients.stream().map( item->( Item )item ).toList() )
				).apply( builder, AccessoryRecipe::new );
			} );
		}

		@Override
		public Codec< AccessoryRecipe > codec() {
			return this.codec;
		}

		public AccessoryRecipe fromNetwork( FriendlyByteBuf buffer ) {
			int size = buffer.readVarInt();
			List< AccessoryItem > ingredients = new ArrayList<>();
			for( int idx = 0; idx < size; ++idx ) {
				ingredients.add( ( AccessoryItem )buffer.readItem().getItem() );
			}

			AccessoryItem result = ( AccessoryItem )buffer.readItem().getItem();
			return new AccessoryRecipe( result, ingredients );
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
