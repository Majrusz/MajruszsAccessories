package com.majruszsaccessories.contexts;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszsaccessories.common.AccessoryHolder;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnAccessoryTooltip {
	public final List< Component > components = new ArrayList<>();
	public final AccessoryHolder holder;

	public static Event< OnAccessoryTooltip > listen( Consumer< OnAccessoryTooltip > consumer ) {
		return Events.get( OnAccessoryTooltip.class ).add( consumer );
	}

	public OnAccessoryTooltip( AccessoryHolder holder ) {
		this.holder = holder;
	}
}
