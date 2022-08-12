package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHandler;
import com.mlib.gamemodifiers.GameModifier;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public abstract class AccessoryModifier extends GameModifier {
	public AccessoryModifier( String configKey, String configName, String configComment ) {
		super( configKey, configName, configComment );
	}

	public abstract void addTooltip( List< MutableComponent > components, AccessoryHandler handler );
}
