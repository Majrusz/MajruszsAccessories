package com.majruszsaccessories.gamemodifiers.configs;

import com.majruszsaccessories.AccessoryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface IAccessoryConfig {
	ChatFormatting DEFAULT_FORMAT = ChatFormatting.GRAY;
	String FORMULA_KEY = "majruszsaccessories.items.formula";

	void addTooltip( String key, List< Component > components, AccessoryHandler handler );

	void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler );
}
