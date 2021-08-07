package com.majruszsaccessories;

import com.majruszsaccessories.items.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.ModLoadingContext;

/** Class that stores all instances of accessory items etc. */
public class Instances {
	public static final CreativeModeTab ITEM_GROUP;

	// Items
	public static final FishermanEmblemItem FISHERMAN_EMBLEM_ITEM;
	public static final LuckyRockItem LUCKY_ROCK_ITEM;
	public static final GiantSeedItem GIANT_SEED_ITEM;
	public static final IdolOfFertilityItem IDOL_OF_FERTILITY_ITEM;
	public static final TamingCertificateItem TAMING_CERTIFICATE_ITEM;
	public static final SecretIngredientItem SECRET_INGREDIENT_ITEM;

	static {
		ITEM_GROUP = new AccessoryItemGroup( "majruszs_accessories_tab" );

		// Items
		FISHERMAN_EMBLEM_ITEM = new FishermanEmblemItem();
		LUCKY_ROCK_ITEM = new LuckyRockItem();
		GIANT_SEED_ITEM = new GiantSeedItem();
		IDOL_OF_FERTILITY_ITEM = new IdolOfFertilityItem();
		TAMING_CERTIFICATE_ITEM = new TamingCertificateItem();
		SECRET_INGREDIENT_ITEM = new SecretIngredientItem();

		MajruszsAccessories.CONFIG_HANDLER.register( ModLoadingContext.get() );
	}
}
