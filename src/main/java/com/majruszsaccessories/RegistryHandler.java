package com.majruszsaccessories;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

/** Class that registers all entities, items etc. */
public class RegistryHandler {
	public static final ResourceLocation ACCESSORY_SLOT_TEXTURE = MajruszsAccessories.getLocation( "item/empty_accessory_slot" );
	public static final DeferredRegister< Item > ITEMS = DeferredRegister.create( ForgeRegistries.ITEMS, MajruszsAccessories.MOD_ID );

	public static void initialize() {
		FMLJavaModLoadingContext loadingContext = FMLJavaModLoadingContext.get();
		final IEventBus modEventBus = loadingContext.getModEventBus();

		registerEverything( modEventBus );

		DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->modEventBus.addListener( RegistryHandler::onTextureStitch ) );
		modEventBus.addListener( RegistryHandler::onEnqueueIMC );
	}

	/** Registers all items. */
	private static void registerItems( final IEventBus modEventBus ) {
		ITEMS.register( "fisherman_emblem", ()->Instances.FISHERMAN_EMBLEM_ITEM );
		ITEMS.register( modEventBus );
	}

	/** Registers everything. */
	private static void registerEverything( final IEventBus modEventBus ) {
		registerItems( modEventBus );
	}

	/** Handles between mods integration. */
	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !Integration.isCuriosInstalled() )
			return;

		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
			()->new SlotTypeMessage.Builder( "pocket" ).priority( 220 )
				.icon( ACCESSORY_SLOT_TEXTURE )
				.build()
		);
	}

	/** Adds custom textures to the game. (curios slot) */
	@OnlyIn( Dist.CLIENT )
	private static void onTextureStitch( TextureStitchEvent.Pre event ) {
		final AtlasTexture map = event.getMap();
		if( PlayerContainer.BLOCK_ATLAS.equals( map.location() ) )
			event.addSprite( ACCESSORY_SLOT_TEXTURE );
	}
}
