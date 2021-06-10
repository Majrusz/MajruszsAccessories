package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItemGroup;
import com.majruszsaccessories.items.FishermanEmblemItem;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.ModLoadingContext;

/** Class that stores all instances of accessory items etc. */
public class Instances {
	public static final ItemGroup ITEM_GROUP;

	// Items
	public static final FishermanEmblemItem FISHERMAN_EMBLEM_ITEM;

	static {
		ITEM_GROUP = new AccessoryItemGroup( "majruszs_accessories_tab" );

		// Items
		FISHERMAN_EMBLEM_ITEM = new FishermanEmblemItem();

		MajruszsAccessories.CONFIG_HANDLER.register( ModLoadingContext.get() );
	}
}
