package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.Registries;
import com.mlib.gamemodifiers.GameModifier;

public class AccessoryModifier extends GameModifier {
	public static final String DEFAULT = Registries.getLocationString( "default" );

	public AccessoryModifier( String configKey, String configName, String configComment ) {
		super( configKey, configName, configComment );
	}
}
