package com.majruszsaccessories;

import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.items.BoosterOverlay;
import com.majruszsaccessories.recipes.AccessoryRecipe;
import com.majruszsaccessories.recipes.BoostAccessoriesRecipe;
import com.majruszsaccessories.recipes.CombineAccessoriesRecipe;
import com.mlib.annotations.AnnotationHandler;
import com.mlib.gamemodifiers.ModConfigs;
import com.mlib.registries.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class Registries {
	private static final RegistryHelper HELPER = new RegistryHelper( MajruszsAccessories.MOD_ID );

	static {
		ModConfigs.init( SERVER_CONFIG, Groups.DEFAULT );
	}

	// Groups
	static final DeferredRegister< Item > ITEMS = HELPER.create( ForgeRegistries.Keys.ITEMS );
	static final DeferredRegister< RecipeSerializer< ? > > RECIPES = HELPER.create( ForgeRegistries.Keys.RECIPE_SERIALIZERS );

	// Items
	public static final RegistryObject< AccessoryItem > ADVENTURERS_GUIDE = ITEMS.register( "adventurers_guide", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > ANGLERS_TROPHY = ITEMS.register( "anglers_trophy", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > CERTIFICATE_OF_TAMING = ITEMS.register( "certificate_of_taming", AccessoryItem::new );
	public static final RegistryObject< BoosterItem > DICE = ITEMS.register( "dice", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > GOLDEN_DICE = ITEMS.register( "golden_dice", BoosterItem.rare() );
	public static final RegistryObject< AccessoryItem > IDOL_OF_FERTILITY = ITEMS.register( "idol_of_fertility", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > LUCKY_ROCK = ITEMS.register( "lucky_rock", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > OVERWORLD_RUNE = ITEMS.register( "overworld_rune", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > SECRET_INGREDIENT = ITEMS.register( "secret_ingredient", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > TAMED_POTATO_BEETLE = ITEMS.register( "tamed_potato_beetle", AccessoryItem::new );
	public static final RegistryObject< AccessoryItem > WHITE_FLAG = ITEMS.register( "white_flag", AccessoryItem::new );

	// Recipes
	public static final RegistryObject< RecipeSerializer< ? > > ACCESSORY_RECIPE = RECIPES.register( "crafting_accessory", AccessoryRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > COMBINE_ACCESSORIES_RECIPE = RECIPES.register( "crafting_combine_accessories", CombineAccessoriesRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > BOOST_ACCESSORIES_RECIPE = RECIPES.register( "crafting_boost_accessories", BoostAccessoriesRecipe.create() );

	// Misc
	public static final ResourceLocation ACCESSORY_SLOT_TEXTURE = Registries.getLocation( "item/empty_accessory_slot" );
	public static final RegistryObject< Item > BOOSTER_OVERLAY = ITEMS.register( "booster_icon", BoosterOverlay::new );

	static {
		new AnnotationHandler( MajruszsAccessories.MOD_ID );
	}

	public static void initialize() {
		FMLJavaModLoadingContext loadingContext = FMLJavaModLoadingContext.get();
		final IEventBus modEventBus = loadingContext.getModEventBus();

		HELPER.registerAll();
		modEventBus.addListener( Registries::onEnqueueIMC );
		SERVER_CONFIG.register( ModLoadingContext.get() );
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}

	public static String getLocationString( String register ) {
		return HELPER.getLocationString( register );
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !Integration.isCuriosInstalled() ) {
			return;
		}

		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket" )
			.priority( 220 )
			.icon( ACCESSORY_SLOT_TEXTURE )
			.build()
		);
	}

	public static class Groups {
		public static final String DEFAULT = Registries.getLocationString( "default" );
	}
}
