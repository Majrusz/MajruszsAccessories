package com.majruszsaccessories.contexts;

import com.majruszsaccessories.items.BoosterItem;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnBoosterTooltip {
	public final List< Component > components = new ArrayList<>();
	public final BoosterItem booster;

	public static Context< OnBoosterTooltip > listen( Consumer< OnBoosterTooltip > consumer ) {
		return Contexts.get( OnBoosterTooltip.class ).add( consumer );
	}

	public OnBoosterTooltip( BoosterItem booster ) {
		this.booster = booster;
	}
}
