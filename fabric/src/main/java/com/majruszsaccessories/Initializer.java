package com.majruszsaccessories;

import com.majruszlibrary.item.CreativeModeTabHelper;
import com.majruszsaccessories.items.CreativeModeTabs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;

public class Initializer implements ModInitializer {
	public static final CreativeModeTab CREATIVE_MODE_TAB = FabricItemGroupBuilder.create( MajruszsAccessories.HELPER.getLocation( "primary" ) )
		.appendItems( ( items, creativeModeTab )->CreativeModeTabs.definePrimaryItems( items::add ) )
		.build();

	@Override
	public void onInitialize() {
		MajruszsAccessories.HELPER.register();

		CreativeModeTabHelper.createItemIconReplacer( CreativeModeTabs::getPrimaryIcons, CREATIVE_MODE_TAB.getDisplayName() );
	}
}
