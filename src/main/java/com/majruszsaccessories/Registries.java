package com.majruszsaccessories;

import com.majruszsaccessories.gamemodifiers.list.AccessoryObserver;
import com.majruszsaccessories.gamemodifiers.list.TooltipUpdater;
import com.majruszsaccessories.gamemodifiers.list.VillagerTradeUpdater;
import com.majruszsaccessories.items.*;
import com.majruszsaccessories.recipes.AccessoryRecipe;
import com.majruszsaccessories.recipes.CombineAccessoriesRecipe;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.registries.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
// import top.theillusivec4.curios.api.CuriosApi;
// import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class Registries {
	private static final RegistryHelper HELPER = new RegistryHelper( MajruszsAccessories.MOD_ID );

	static {
		SERVER_CONFIG.addGroup( GameModifier.addNewGroup( Modifiers.DEFAULT_GROUP ) );
	}

	// Groups
	static final DeferredRegister< Item > ITEMS = HELPER.create( ForgeRegistries.Keys.ITEMS );
	static final DeferredRegister< RecipeSerializer< ? > > RECIPES = HELPER.create( ForgeRegistries.Keys.RECIPE_SERIALIZERS );

	// Items
	public static final RegistryObject< AdventurersGuideItem > ADVENTURERS_GUIDE = ITEMS.register( "adventurers_guide", AdventurersGuideItem.create() );
	public static final RegistryObject< AnglersTrophyItem > ANGLERS_TROPHY = ITEMS.register( "anglers_trophy", AnglersTrophyItem.create() );
	public static final RegistryObject< CertificateOfTamingItem > CERTIFICATE_OF_TAMING = ITEMS.register( "certificate_of_taming", CertificateOfTamingItem.create() );
	public static final RegistryObject< IdolOfFertilityItem > IDOL_OF_FERTILITY = ITEMS.register( "idol_of_fertility", IdolOfFertilityItem.create() );
	public static final RegistryObject< LuckyRockItem > LUCKY_ROCK = ITEMS.register( "lucky_rock", LuckyRockItem.create() );
	public static final RegistryObject< OverworldRuneItem > OVERWORLD_RUNE = ITEMS.register( "overworld_rune", OverworldRuneItem.create() );
	public static final RegistryObject< SecretIngredientItem > SECRET_INGREDIENT = ITEMS.register( "secret_ingredient", SecretIngredientItem.create() );
	public static final RegistryObject< TamedPotatoBeetleItem > TAMED_POTATO_BEETLE = ITEMS.register( "tamed_potato_beetle", TamedPotatoBeetleItem.create() );
	public static final RegistryObject< WhiteFlagItem > WHITE_FLAG = ITEMS.register( "white_flag", WhiteFlagItem.create() );

	// Recipes
	public static final RegistryObject< RecipeSerializer< ? > > ACCESSORY_RECIPE = RECIPES.register( "crafting_accessory", AccessoryRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > COMBINE_ACCESSORIES_RECIPE = RECIPES.register( "crafting_combine_accessories", CombineAccessoriesRecipe.create() );

	// Misc
	public static final ResourceLocation ACCESSORY_SLOT_TEXTURE = Registries.getLocation( "item/empty_accessory_slot" );

	// Game Modifiers
	public static final List< GameModifier > GAME_MODIFIERS = new ArrayList<>();

	static {
		GAME_MODIFIERS.add( new AccessoryObserver() );
		GAME_MODIFIERS.add( new TooltipUpdater() );
		GAME_MODIFIERS.add( new VillagerTradeUpdater() );
	}

	public static void initialize() {
		FMLJavaModLoadingContext loadingContext = FMLJavaModLoadingContext.get();
		final IEventBus modEventBus = loadingContext.getModEventBus();

		HELPER.registerAll();
		// DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->modEventBus.addListener( Registries::onTextureStitch ) );
		modEventBus.addListener( Registries::onEnqueueIMC );
		SERVER_CONFIG.register( ModLoadingContext.get() );
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}

	public static String getLocationString( String register ) {
		return getLocation( register ).toString();
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !Integration.isCuriosInstalled() ) {
			return;
		}

		/*Supplier< SlotTypeMessage > slotTypeMessage = ()->new SlotTypeMessage.Builder( "pocket" ).priority( 220 )
			.icon( ACCESSORY_SLOT_TEXTURE )
			.build();
		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, slotTypeMessage );*/
	}

	/*@OnlyIn( Dist.CLIENT )
	private static void onTextureStitch( TextureStitchEvent.Pre event ) {
		if( InventoryMenu.BLOCK_ATLAS.equals( event.getAtlas().location() ) ) {
			event.addSprite( ACCESSORY_SLOT_TEXTURE );
		}
	}*/

	public static class Modifiers {
		public static final String DEFAULT_GROUP = Registries.getLocationString( "default" );
	}
}
