package com.majruszsaccessories;

import com.majruszlibrary.item.CreativeModeTabHelper;
import com.majruszsaccessories.items.CreativeModeTabs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;

public class Initializer implements ModInitializer {
	public static final CreativeModeTab CREATIVE_MODE_TAB = FabricItemGroup.builder( MajruszsAccessories.HELPER.getLocation( "primary" ) )
		.title( CreativeModeTabs.PRIMARY )
		.displayItems( ( features, entries )->CreativeModeTabs.definePrimaryItems( entries::accept ) )
		.build();

	@Override
	public void onInitialize() {
		MajruszsAccessories.HELPER.register();

		CreativeModeTabHelper.createItemIconReplacer( CreativeModeTabs::getPrimaryIcons, CreativeModeTabs.PRIMARY );
	}
}
