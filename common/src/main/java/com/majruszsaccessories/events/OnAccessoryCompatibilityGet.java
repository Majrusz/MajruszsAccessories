package com.majruszsaccessories.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszsaccessories.items.AccessoryItem;

import java.util.function.Consumer;

public class OnAccessoryCompatibilityGet {
	public final AccessoryItem a;
	public final AccessoryItem b;
	private boolean areIncompatible;

	public static Event< OnAccessoryCompatibilityGet > listen( Consumer< OnAccessoryCompatibilityGet > consumer ) {
		return Events.get( OnAccessoryCompatibilityGet.class ).add( consumer );
	}

	public OnAccessoryCompatibilityGet( AccessoryItem a, AccessoryItem b ) {
		this.a = a;
		this.b = b;
		this.areIncompatible = a.equals( b );
	}

	public void makeIncompatible() {
		this.areIncompatible = true;
	}

	public boolean areIncompatible() {
		return this.areIncompatible;
	}
}
