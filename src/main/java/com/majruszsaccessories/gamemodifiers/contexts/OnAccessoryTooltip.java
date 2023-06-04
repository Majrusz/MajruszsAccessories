package com.majruszsaccessories.gamemodifiers.contexts;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OnAccessoryTooltip {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ItemStack itemStack ) {
		return Contexts.get( Data.class ).dispatch( new Data( itemStack ) );
	}

	public static Condition< Data > is( Supplier< AccessoryItem > item ) {
		return Condition.predicate( data->data.holder.getItem().equals( item.get() ) );
	}

	public static class Data {
		public final List< Component > components = new ArrayList<>();
		public final AccessoryHolder holder;

		public Data( ItemStack itemStack ) {
			this.holder = AccessoryHolder.create( itemStack );
		}
	}
}
