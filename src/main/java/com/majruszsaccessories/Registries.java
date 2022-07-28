package com.majruszsaccessories;

import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AnglerEmblemItem;
import com.majruszsdifficulty.items.CustomItemGroup;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.items.ItemCreativeModeTab;
import com.mlib.registries.DeferredRegisterHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class Registries {
	private static final DeferredRegisterHelper HELPER = new DeferredRegisterHelper( MajruszsAccessories.MOD_ID );

	static {
		CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( AccessoryModifier.DEFAULT ) );
	}

	// Groups
	static final DeferredRegister< Item > ITEMS = HELPER.create( ForgeRegistries.Keys.ITEMS );

	// Items
	public static final RegistryObject< AnglerEmblemItem > ANGLER_EMBLEM = ITEMS.register( "angler_emblem", AnglerEmblemItem::new );

	// Misc
	public static final CreativeModeTab ITEM_GROUP = new ItemCreativeModeTab( "majruszs_accessories_tab", ANGLER_EMBLEM );
	public static final ResourceLocation ACCESSORY_SLOT_TEXTURE = Registries.getLocation( "item/empty_accessory_slot" );

	public static void initialize() {
		FMLJavaModLoadingContext loadingContext = FMLJavaModLoadingContext.get();
		final IEventBus modEventBus = loadingContext.getModEventBus();

		HELPER.registerAll();
		DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->modEventBus.addListener( Registries::onTextureStitch ) );
		modEventBus.addListener( Registries::onEnqueueIMC );
		CONFIG_HANDLER.register( ModLoadingContext.get() );
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}

	public static String getLocationString( String register ) {
		return getLocation( register ).toString();
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !Integration.isCuriosInstalled() ) {
			return;
		}

		Supplier< SlotTypeMessage > slotTypeMessage = ()->new SlotTypeMessage.Builder( "pocket" ).priority( 220 ).icon( ACCESSORY_SLOT_TEXTURE ).build();
		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, slotTypeMessage );
	}

	@OnlyIn( Dist.CLIENT )
	private static void onTextureStitch( TextureStitchEvent.Pre event ) {
		if( InventoryMenu.BLOCK_ATLAS.equals( event.getAtlas().location() ) ) {
			event.addSprite( ACCESSORY_SLOT_TEXTURE );
		}
	}
}
