package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.common.ComponentBase;
import com.majruszsaccessories.boosters.BoosterItem;
import com.mlib.config.ConfigGroup;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BoosterComponent extends ComponentBase< BoosterItem > {
	public BoosterComponent( Supplier< BoosterItem > item ) {
		super( item );
	}

	@FunctionalInterface
	public interface ISupplier extends BiFunction< Supplier< BoosterItem >, ConfigGroup, BoosterComponent > {}
}
