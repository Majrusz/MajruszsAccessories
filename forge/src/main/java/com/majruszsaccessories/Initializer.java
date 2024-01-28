package com.majruszsaccessories;

import com.majruszlibrary.item.CreativeModeTabHelper;
import com.majruszsaccessories.items.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod( MajruszsAccessories.MOD_ID )
public class Initializer {
	public Initializer() {
		MajruszsAccessories.HELPER.register();
		MinecraftForge.EVENT_BUS.register( this );

		FMLJavaModLoadingContext.get().getModEventBus().addListener( Initializer::onEnqueueIMC );
		FMLJavaModLoadingContext.get().getModEventBus().addListener( Initializer::registerTabs );
		CreativeModeTabHelper.createItemIconReplacer( CreativeModeTabs::getPrimaryIcons, CreativeModeTabs.PRIMARY );
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !MajruszsAccessories.SLOT_INTEGRATION.isInstalled() ) {
			return;
		}

		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket" )
			.priority( 220 )
			.icon( MajruszsAccessories.POCKET_SLOT_TEXTURE )
			.build()
		);
	}

	private static void registerTabs( CreativeModeTabEvent.Register event ) {
		event.registerCreativeModeTab( MajruszsAccessories.HELPER.getLocation( "primary" ), builder->{
			builder.title( CreativeModeTabs.PRIMARY )
				.displayItems( ( features, entries )->CreativeModeTabs.definePrimaryItems( entries::accept ) );
		} );
	}
}
