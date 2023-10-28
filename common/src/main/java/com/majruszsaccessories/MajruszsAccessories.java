package com.majruszsaccessories;

import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.config.Config;
import com.majruszsaccessories.integration.ISlotPlatform;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.items.BoosterOverlay;
import com.majruszsaccessories.items.CreativeModeTabs;
import com.majruszsaccessories.particles.BonusParticle;
import com.majruszsaccessories.recipes.AccessoryRecipe;
import com.majruszsaccessories.recipes.BoostAccessoriesRecipe;
import com.majruszsaccessories.recipes.CombineAccessoriesRecipe;
import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.contexts.OnGameInitialized;
import com.mlib.contexts.OnParticlesRegistered;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.math.Random;
import com.mlib.modhelper.ModHelper;
import com.mlib.network.NetworkObject;
import com.mlib.platform.Services;
import com.mlib.registry.RegistryGroup;
import com.mlib.registry.RegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;
import java.util.function.Supplier;

public class MajruszsAccessories {
	public static final String MOD_ID = "majruszsaccessories";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );

	// Configs
	public static final Config CONFIG = HELPER.config( Config::new ).autoSync().create();

	// Registry Groups
	public static final RegistryGroup< Item > ITEMS = HELPER.create( BuiltInRegistries.ITEM );
	public static final RegistryGroup< CreativeModeTab > CREATIVE_MODE_TABS = HELPER.create( BuiltInRegistries.CREATIVE_MODE_TAB );
	public static final RegistryGroup< RecipeSerializer< ? > > RECIPES = HELPER.create( BuiltInRegistries.RECIPE_SERIALIZER );
	public static final RegistryGroup< ParticleType< ? > > PARTICLES = HELPER.create( BuiltInRegistries.PARTICLE_TYPE );

	// Items (Accessories)
	public static final RegistryObject< AccessoryItem > ADVENTURER_KIT = ITEMS.create( "adventurer_kit", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > ADVENTURER_RUNE = ITEMS.create( "adventurer_rune", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > ANCIENT_SCARAB = ITEMS.create( "ancient_scarab", AccessoryItem.tier1() );
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
	public static final RegistryObject< AccessoryItem > PEACE_EMBLEM = ITEMS.create( "peace_emblem", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > PEACE_TREATY = ITEMS.create( "peace_treaty", AccessoryItem.tier2() );
	public static final RegistryObject< AccessoryItem > SECRET_INGREDIENT = ITEMS.create( "secret_ingredient", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > SWIMMER_GUIDE = ITEMS.create( "swimmer_guide", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > TAMED_POTATO_BEETLE = ITEMS.create( "tamed_potato_beetle", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > TOOL_SCRAPS = ITEMS.create( "tool_scraps", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > UNBREAKABLE_FISHING_LINE = ITEMS.create( "unbreakable_fishing_line", AccessoryItem.tier1() );
	public static final RegistryObject< AccessoryItem > WHITE_FLAG = ITEMS.create( "white_flag", AccessoryItem.tier1() );

	// Items (Boosters)
	public static final RegistryObject< BoosterItem > DICE = ITEMS.create( "dice", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > GOLDEN_DICE = ITEMS.create( "golden_dice", BoosterItem.rare() );
	public static final RegistryObject< BoosterItem > GOLDEN_HORSESHOE = ITEMS.create( "golden_horseshoe", BoosterItem.rare() );
	public static final RegistryObject< BoosterItem > HORSESHOE = ITEMS.create( "horseshoe", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > JADEITE = ITEMS.create( "jadeite", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > ONYX = ITEMS.create( "onyx", BoosterItem.basic() );
	public static final RegistryObject< BoosterItem > OWL_FEATHER = ITEMS.create( "owl_feather", BoosterItem.basic() );

	// Items (Fake)
	public static final RegistryObject< BoosterOverlay > BOOSTER_OVERLAY_SINGLE = ITEMS.create( "booster_overlay_single", BoosterOverlay::new );
	public static final RegistryObject< BoosterOverlay > BOOSTER_OVERLAY_DOUBLE = ITEMS.create( "booster_overlay_double", BoosterOverlay::new );
	public static final RegistryObject< BoosterOverlay > BOOSTER_OVERLAY_TRIPLE = ITEMS.create( "booster_overlay_triple", BoosterOverlay::new );

	// Recipes
	public static final RegistryObject< RecipeSerializer< ? > > ACCESSORY_RECIPE = RECIPES.create( "crafting_accessory", AccessoryRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > COMBINE_ACCESSORIES_RECIPE = RECIPES.create( "crafting_combine_accessories", CombineAccessoriesRecipe.create() );
	public static final RegistryObject< RecipeSerializer< ? > > BOOST_ACCESSORIES_RECIPE = RECIPES.create( "crafting_boost_accessories", BoostAccessoriesRecipe.create() );

	// Particles
	public static final RegistryObject< SimpleParticleType > BONUS_WEAK_PARTICLE = PARTICLES.create( "bonus_weak", ()->new SimpleParticleType( true ) {} );
	public static final RegistryObject< SimpleParticleType > BONUS_NORMAL_PARTICLE = PARTICLES.create( "bonus_normal", ()->new SimpleParticleType( true ) {} );
	public static final RegistryObject< SimpleParticleType > BONUS_STRONG_PARTICLE = PARTICLES.create( "bonus_strong", ()->new SimpleParticleType( true ) {} );

	// Creative Mode Tabs
	public static final RegistryObject< CreativeModeTab > CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.create( "primary", CreativeModeTabs.primary() );

	// Network
	public static final NetworkObject< MoreChestLoot.BonusInfo > MORE_CHEST_LOOT = HELPER.create( "more_chest_loot", MoreChestLoot.BonusInfo.class );

	// Integration
	public static final ISlotPlatform SLOT_INTEGRATION = Services.load( ISlotPlatform.class );

	// Textures
	public static final ResourceLocation POCKET_SLOT_TEXTURE = HELPER.getLocation( "slot/pocket" );

	static {
		OnGameInitialized.listen( MajruszsAccessories::setDefaultEmitters );
	}

	private static void setDefaultEmitters( OnGameInitialized data ) {
		for( Supplier< SimpleParticleType > particle : List.of( BONUS_WEAK_PARTICLE, BONUS_NORMAL_PARTICLE, BONUS_STRONG_PARTICLE ) ) {
			ParticleEmitter.setDefault( particle.get(), new ParticleEmitter.Properties( ParticleEmitter.offset( 0.25f ), ()->Random.nextSign() * Random.nextFloat( 0.005f, 0.001f ) ) );
		}
	}

	private MajruszsAccessories() {}

	@OnlyIn( Dist.CLIENT )
	public static class Client {
		static {
			OnParticlesRegistered.listen( data->data.register( BONUS_WEAK_PARTICLE.get(), spriteSet->new BonusParticle.Factory( spriteSet, Rarity.UNCOMMON.color.getColor() ) ) );
			OnParticlesRegistered.listen( data->data.register( BONUS_NORMAL_PARTICLE.get(), spriteSet->new BonusParticle.Factory( spriteSet, Rarity.RARE.color.getColor() ) ) );
			OnParticlesRegistered.listen( data->data.register( BONUS_STRONG_PARTICLE.get(), spriteSet->new BonusParticle.Factory( spriteSet, Rarity.EPIC.color.getColor() ) ) );
		}
	}
}
