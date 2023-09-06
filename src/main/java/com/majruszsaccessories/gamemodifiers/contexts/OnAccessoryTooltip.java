package com.majruszsaccessories.gamemodifiers.contexts;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnAccessoryTooltip {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ItemStack itemStack ) {
		return Contexts.get( Data.class ).dispatch( new Data( itemStack ) );
	}

	public static class Data {
		public final List< Component > components = new ArrayList<>();
		public final AccessoryHolder holder;

		public Data( ItemStack itemStack ) {
			this.holder = AccessoryHolder.create( itemStack );
		}
	}
}
