package com.majruszsaccessories.gamemodifiers.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber( value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD )
public class OnItemRender {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( RegisterItemDecorationsEvent item ) {
		return Contexts.get( Data.class ).dispatch( new Data( item ) );
	}

	@SubscribeEvent
	public static void onDecorator( RegisterItemDecorationsEvent event ) {
		OnItemRender.dispatch( event );
	}

	public static class Data {
		private final RegisterItemDecorationsEvent event;

		public Data( RegisterItemDecorationsEvent event ) {
			this.event = event;
		}

		public < Type extends Item > void addDecoration( Supplier< Type > item, IItemDecorator decorator ) {
			this.event.register( item.get(), decorator );
		}
	}
}
