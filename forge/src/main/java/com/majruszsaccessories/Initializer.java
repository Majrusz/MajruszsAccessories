package com.majruszsaccessories;

import com.majruszlibrary.item.CreativeModeTabHelper;
import com.majruszsaccessories.items.CreativeModeTabs;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod( MajruszsAccessories.MOD_ID )
public class Initializer {
	public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab( "majruszsaccessories.primary" ) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack( MajruszsAccessories.ANGLER_TROPHY.get() );
		}

		@Override
		public void fillItemList( NonNullList< ItemStack > stacks ) {
			CreativeModeTabs.definePrimaryItems( stacks::add );
		}
	};

	public Initializer() {
		MajruszsAccessories.HELPER.register();
		MinecraftForge.EVENT_BUS.register( this );

		FMLJavaModLoadingContext.get().getModEventBus().addListener( Initializer::onEnqueueIMC );
		DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->FMLJavaModLoadingContext.get().getModEventBus().addListener( Initializer::onTextureStitch ) );
		CreativeModeTabHelper.createItemIconReplacer( CreativeModeTabs::getPrimaryIcons, CREATIVE_MODE_TAB.getDisplayName() );
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !MajruszsAccessories.SLOT_INTEGRATION.isInstalled() ) {
			return;
		}

		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket_left" )
			.priority( 220 )
			.icon( MajruszsAccessories.POCKET_SLOT_TEXTURE )
			.build()
		);
		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket_right" )
			.priority( 220 )
			.icon( MajruszsAccessories.POCKET_SLOT_TEXTURE )
			.build()
		);
	}

	@OnlyIn( Dist.CLIENT )
	private static void onTextureStitch( TextureStitchEvent.Pre event ) {
		final TextureAtlas map = event.getAtlas();
		if( InventoryMenu.BLOCK_ATLAS.equals( map.location() ) ) {
			event.addSprite( MajruszsAccessories.HELPER.getLocation( "slot/pocket" ) );
		}
	}

}
