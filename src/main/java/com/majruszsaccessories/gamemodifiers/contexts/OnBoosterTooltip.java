package com.majruszsaccessories.gamemodifiers.contexts;

import com.majruszsaccessories.boosters.BoosterItem;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnBoosterTooltip {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( BoosterItem item ) {
		return Contexts.get( Data.class ).dispatch( new Data( item ) );
	}

	public static class Data {
		public final List< Component > components = new ArrayList<>();
		public final BoosterItem item;

		public Data( BoosterItem item ) {
			this.item = item;
		}
	}
}
