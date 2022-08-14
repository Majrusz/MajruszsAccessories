package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AccessoryModifier extends GameModifier {
	protected static final ChatFormatting DEFAULT_FORMAT = ChatFormatting.GRAY;
	protected static final String FORMULA_KEY = "majruszsaccessories.items.formula";
	protected final Supplier< ? extends AccessoryItem > item;

	public AccessoryModifier( Supplier< ? extends AccessoryItem > item, String configKey, String configName, String configComment ) {
		super( configKey, configName, configComment );
		this.item = item;
	}

	public abstract void addTooltip( List< Component > components, AccessoryHandler handler );

	public abstract void addDetailedTooltip( List< Component > components, AccessoryHandler handler );

	protected MutableComponent getBonus( Supplier< Integer > defaultBonus, Function< AccessoryHandler, Integer > bonus, AccessoryHandler handler ) {
		MutableComponent component = Component.literal( String.format( "%d", bonus.apply( handler ) ) );
		int diff = bonus.apply( handler ) - defaultBonus.get();
		component.withStyle( diff != 0 ? handler.getBonusFormatting() : DEFAULT_FORMAT );

		return component;
	}

	protected MutableComponent getFormula( Supplier< Integer > defaultBonus, Function< AccessoryHandler, Integer > bonus, AccessoryHandler handler ) {
		FormattedTranslatable component = new FormattedTranslatable( FORMULA_KEY, DEFAULT_FORMAT );
		component.addParameter( defaultBonus.get().toString() );
		int diff = bonus.apply( handler ) - defaultBonus.get();
		if( diff != 0 ) {
			component.addParameter( TextHelper.signed( diff ), handler.getBonusFormatting() );
		}

		return component.create();
	}
}
