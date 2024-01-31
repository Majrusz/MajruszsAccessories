package com.majruszsaccessories;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.emitter.ParticleEmitter;
import com.majruszlibrary.events.OnGameInitialized;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.modhelper.ModHelper;
import com.majruszlibrary.network.NetworkObject;
import com.majruszlibrary.platform.Services;
import com.majruszlibrary.registry.Custom;
import com.majruszlibrary.registry.RegistryGroup;
import com.majruszlibrary.registry.RegistryObject;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.cards.GamblingCard;
import com.majruszsaccessories.cards.RemovalCard;
import com.majruszsaccessories.cards.ReverseCard;
import com.majruszsaccessories.config.Config;
import com.majruszsaccessories.integration.ISlotPlatform;
import com.majruszsaccessories.items.*;
import com.majruszsaccessories.particles.BonusParticle;
import com.majruszsaccessories.particles.BonusParticleType;
import com.majruszsaccessories.recipes.AccessoryRecipe;
import com.majruszsaccessories.recipes.BoostAccessoryRecipe;
import com.majruszsaccessories.recipes.CombineAccessoriesRecipe;
import com.majruszsaccessories.recipes.UseCardRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MajruszsAccessories {
	public static final String MOD_ID = "majruszsaccessories";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );

	// Configs
	static {
		HELPER.config( Config.class ).autoSync().create();
	}

	// Registry Groups
	public static final RegistryGroup< Item > ITEMS = HELPER.create( Registry.ITEM );
	public static final RegistryGroup< RecipeSerializer< ? > > RECIPES = HELPER.create( Registry.RECIPE_SERIALIZER );
	public static final RegistryGroup< ParticleType< ? > > PARTICLES = HELPER.create( Registry.PARTICLE_TYPE );

	// Network
	public static final NetworkObject< MoreChestLoot.BonusInfo > MORE_CHEST_LOOT = HELPER.create( "more_chest_loot", MoreChestLoot.BonusInfo.class );

	// Items (Accessories)
	public static final RegistryObject< AccessoryItem > ADVENTURER_KIT = ITEMS.create( "adventurer_kit", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > ADVENTURER_RUNE = ITEMS.create( "adventurer_rune", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > ANGLER_RUNE = ITEMS.create( "angler_rune", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > ANGLER_TROPHY = ITEMS.create( "angler_trophy", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > CERTIFICATE_OF_TAMING = ITEMS.create( "certificate_of_taming", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > DISCOUNT_VOUCHER = ITEMS.create( "discount_voucher", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > DREAM_CATCHER = ITEMS.create( "dream_catcher", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > HOUSEHOLD_RUNE = ITEMS.create( "household_rune", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > IDOL_OF_FERTILITY = ITEMS.create( "idol_of_fertility", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > LUCKY_ROCK = ITEMS.create( "lucky_rock", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > METAL_LURE = ITEMS.create( "metal_lure", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > MINER_GUIDE = ITEMS.create( "miner_guide", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > MINER_RUNE = ITEMS.create( "miner_rune", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > NATURE_RUNE = ITEMS.create( "nature_rune", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > SECRET_INGREDIENT = ITEMS.create( "secret_ingredient", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > SOUL_OF_MINECRAFT = ITEMS.create( "soul_of_minecraft", AccessoryItem.tier3() );
	public static final RegistryObject< AccessoryItem > SWIMMER_GUIDE = ITEMS.create( "swimmer_guide", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > TAMED_POTATO_BEETLE = ITEMS.create( "tamed_potato_beetle", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > TOOL_SCRAPS = ITEMS.create( "tool_scraps", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > UNBREAKABLE_FISHING_LINE = ITEMS.create( "unbreakable_fishing_line", AccessoryItem.tier1() );

	// Items (Boosters)
	public static final RegistryObject< BoosterItem > DICE = ITEMS.create( "dice", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > GOLDEN_DICE = ITEMS.create( "golden_dice", BoosterItem.rare() );
	public static final RegistryObject< BoosterItem > GOLDEN_HORSESHOE = ITEMS.create( "golden_horseshoe", BoosterItem.rare() );
	public static final RegistryObject< BoosterItem > HORSESHOE = ITEMS.create( "horseshoe", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > ONYX = ITEMS.create( "onyx", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > OWL_FEATHER = ITEMS.create( "owl_feather", BoosterItem.basic() );

	// Items (Cards)
	public static final RegistryObject< CardItem > GAMBLING_CARD = ITEMS.create( "gambling_card", GamblingCard::new );
	public static final RegistryObject< CardItem > REMOVAL_CARD = ITEMS.create( "removal_card", RemovalCard::new );
	public static final RegistryObject< CardItem > REVERSE_CARD = ITEMS.create( "reverse_card", ReverseCard::new );

	// Items (Fake)
	public static final RegistryObject< BoosterOverlay > BOOSTER_OVERLAY_SINGLE = ITEMS.create( "booster_overlay_single", BoosterOverlay::new );
	public static final RegistryObject< BoosterOverlay > BOOSTER_OVERLAY_DOUBLE = ITEMS.create( "booster_overlay_double", BoosterOverlay::new );
	public static final RegistryObject< BoosterOverlay > BOOSTER_OVERLAY_TRIPLE = ITEMS.create( "booster_overlay_triple", BoosterOverlay::new );

	// Recipes
	public static final RegistryObject< RecipeSerializer< ? > > ACCESSORY_RECIPE = RECIPES.create( "crafting_accessory", AccessoryRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > BOOST_ACCESSORY_RECIPE = RECIPES.create( "crafting_boost_accessory", BoostAccessoryRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > COMBINE_ACCESSORIES_RECIPE = RECIPES.create( "crafting_combine_accessories", CombineAccessoriesRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > USE_CARD_RECIPE = RECIPES.create( "crafting_use_card", UseCardRecipe.create() );

	// Particles
	public static final RegistryObject< BonusParticleType > BONUS_PARTICLE = PARTICLES.create( "bonus_normal", BonusParticleType::new );

	// Integration
	public static final ISlotPlatform SLOT_INTEGRATION = Services.load( ISlotPlatform.class );

	// Textures
	public static final ResourceLocation POCKET_SLOT_TEXTURE = HELPER.getLocation( "slot/pocket" );

	static {
		OnGameInitialized.listen( MajruszsAccessories::setDefaultEmitters );
	}

	private static void setDefaultEmitters( OnGameInitialized data ) {
		ParticleEmitter.setDefault( BONUS_PARTICLE.get(), new ParticleEmitter.Properties( ParticleEmitter.offset( 0.25f ), ()->Random.nextSign() * Random.nextFloat( 0.005f, 0.001f ) ) );
	}

	private MajruszsAccessories() {}

	@OnlyIn( Dist.CLIENT )
	public static class Client {
		static {
			HELPER.create( Custom.Particles.class, particles->{
				particles.register( BONUS_PARTICLE.get(), BonusParticle.Factory::new );
			} );
		}
	}
}
