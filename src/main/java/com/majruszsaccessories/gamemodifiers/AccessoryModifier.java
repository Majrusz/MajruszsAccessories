package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.GameModifier;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public abstract class AccessoryModifier extends GameModifier {
	protected static final String FORMULA_KEY = "majruszsaccessories.items.formula";
	protected final Supplier< ? extends AccessoryItem > item;

	public AccessoryModifier( Supplier< ? extends AccessoryItem > item, String configKey, String configName, String configComment ) {
		super( configKey, configName, configComment );
		this.item = item;
	}

	public abstract void addTooltip( List< Component > components, AccessoryHandler handler );

	public abstract void addDetailedTooltip( List< Component > components, AccessoryHandler handler );
}
