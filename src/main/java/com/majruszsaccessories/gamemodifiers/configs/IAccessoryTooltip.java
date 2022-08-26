package com.majruszsaccessories.gamemodifiers.configs;

import com.majruszsaccessories.AccessoryHandler;
import com.mlib.text.FormattedTranslatable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface IAccessoryTooltip {
	ChatFormatting DEFAULT_FORMAT = ChatFormatting.GRAY;
	String FORMULA_KEY = "majruszsaccessories.items.formula";

	void addTooltip( String key, List< Component > components, AccessoryHandler handler );

	default void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		this.addTooltip( key, components, handler );
	}

	default boolean areTooltipsIdentical( AccessoryHandler handler ) {
		return true;
	}

	static FormattedTranslatable build( String id, ChatFormatting... defaultFormat ) {
		return new FormattedTranslatable( id, defaultFormat );
	}
}
