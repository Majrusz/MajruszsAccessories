package com.majruszsaccessories;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
//import top.theillusivec4.curios.api.CuriosApi;
//import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod( MajruszsAccessories.MOD_ID )
public class Initializer {
	public Initializer() {
		MajruszsAccessories.HELPER.register();

		FMLJavaModLoadingContext.get().getModEventBus().addListener( Initializer::onEnqueueIMC );
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !MajruszsAccessories.SLOT_INTEGRATION.isInstalled() ) {
			return;
		}

//		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket" )
//			.priority( 220 )
//			.icon( MajruszsAccessories.POCKET_SLOT_TEXTURE )
//			.build()
//		);
	}
}
