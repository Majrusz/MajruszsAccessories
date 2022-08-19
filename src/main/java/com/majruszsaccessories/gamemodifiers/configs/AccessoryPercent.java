package com.majruszsaccessories.gamemodifiers.configs;

import com.majruszsaccessories.AccessoryHandler;
import com.mlib.config.DoubleConfig;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoryPercent extends DoubleConfig implements IAccessoryConfig {
	final float multiplier;

	public AccessoryPercent( String name, String comment, boolean worldRestartRequired, double defaultValue, double min, double max, double multiplier ) {
		super( name, comment, worldRestartRequired, defaultValue, min, max );
		this.multiplier = ( float )multiplier;
	}

	public AccessoryPercent( String name, String comment, boolean worldRestartRequired, double defaultValue, double min, double max ) {
		this( name, comment, worldRestartRequired, defaultValue, min, max, 1.0 );
	}

	public float getDefaultValue() {
		return this.asFloat();
	}

	public float getValue( AccessoryHandler handler ) {
		return handler != null ? ( 1.0f + this.multiplier * handler.getBonus() ) * this.getDefaultValue() : 0.0f;
	}

	public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		FormattedTranslatable component = new FormattedTranslatable( key, DEFAULT_FORMAT );
		component.addParameter( this.getPercentBonus( this::getDefaultValue, this::getValue, handler ) ).insertInto( components );
	}

	public void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		if( Math.abs( this.getDefaultValue() - this.getValue( handler ) ) < 0.001 ) {
			this.addTooltip( key, components, handler );
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( key, DEFAULT_FORMAT );
		component.addParameter( this.getPercentFormula( this::getDefaultValue, this::getValue, handler ) ).insertInto( components );
	}

	private MutableComponent getPercentBonus( Supplier< Float > defaultBonus, Function< AccessoryHandler, Float > bonus, AccessoryHandler handler ) {
		MutableComponent component = Component.literal( TextHelper.percent( bonus.apply( handler ) ) );
		float diff = bonus.apply( handler ) - defaultBonus.get();
		component.withStyle( Math.abs( diff ) >= 0.001f ? handler.getBonusFormatting() : DEFAULT_FORMAT );

		return component;
	}

	private MutableComponent getPercentFormula( Supplier< Float > defaultBonus, Function< AccessoryHandler, Float > bonus, AccessoryHandler handler ) {
		FormattedTranslatable component = new FormattedTranslatable( FORMULA_KEY, DEFAULT_FORMAT );
		component.addParameter( TextHelper.percent( defaultBonus.get() ) );
		float diff = bonus.apply( handler ) - defaultBonus.get();
		if( Math.abs( diff ) >= 0.001f ) {
			component.addParameter( TextHelper.signedPercent( diff ), handler.getBonusFormatting() );
		}

		return component.create();
	}
}
