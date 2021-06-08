package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.AccessoryItemGroup;
import com.majruszsaccessories.items.FishermanEmblemItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.fml.ModLoadingContext;

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
