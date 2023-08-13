package com.majruszsaccessories.gamemodifiers.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: 1.18.2 does not support IItemDecorator
public class OnItemRender {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch() {
		return Contexts.get( Data.class ).dispatch( new Data() );
	}

	public static void onDecorator() {
		OnItemRender.dispatch();
	}

	public static class Data {
		public Data() {}

		public < Type extends Item > void addDecoration( Supplier< Type > item ) {}
	}
}
