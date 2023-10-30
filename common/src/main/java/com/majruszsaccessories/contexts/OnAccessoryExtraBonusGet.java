package com.majruszsaccessories.contexts;

import com.majruszsaccessories.common.AccessoryHolder;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnAccessoryExtraBonusGet {
	public final AccessoryHolder holder;
	public final float original = 0.0f;
	public float bonus = 0.0f;

	public static Context< OnAccessoryExtraBonusGet > listen( Consumer< OnAccessoryExtraBonusGet > consumer ) {
		return Contexts.get( OnAccessoryExtraBonusGet.class ).add( consumer );
	}

	public OnAccessoryExtraBonusGet( AccessoryHolder holder ) {
		this.holder = holder;
	}
}
