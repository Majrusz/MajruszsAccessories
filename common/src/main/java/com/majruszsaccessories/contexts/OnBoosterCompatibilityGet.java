package com.majruszsaccessories.contexts;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszsaccessories.items.BoosterItem;

import java.util.function.Consumer;

public class OnBoosterCompatibilityGet {
	public final BoosterItem a;
	public final BoosterItem b;
	private boolean areIncompatible;

	public static Event< OnBoosterCompatibilityGet > listen( Consumer< OnBoosterCompatibilityGet > consumer ) {
		return Events.get( OnBoosterCompatibilityGet.class ).add( consumer );
	}

	public OnBoosterCompatibilityGet( BoosterItem a, BoosterItem b ) {
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
