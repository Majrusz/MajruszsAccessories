package com.majruszsaccessories.contexts;

import com.majruszsaccessories.common.AccessoryHolder;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnAccessoryTooltip {
	public final List< Component > components = new ArrayList<>();
	public final AccessoryHolder holder;

	public static Context< OnAccessoryTooltip > listen( Consumer< OnAccessoryTooltip > consumer ) {
		return Contexts.get( OnAccessoryTooltip.class ).add( consumer );
	}

	public OnAccessoryTooltip( AccessoryHolder holder ) {
		this.holder = holder;
	}
}
