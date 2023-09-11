package com.majruszsaccessories.gamemodifiers.contexts;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnAccessoryExtraBonusGet {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( AccessoryHolder holder ) {
		return Contexts.get( Data.class ).dispatch( new Data( holder ) );
	}

	public static class Data {
		public final AccessoryHolder holder;
		public float value = 0.0f;

		public Data( AccessoryHolder holder ) {
			this.holder = holder;
		}
	}
}
