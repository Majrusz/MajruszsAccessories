package com.majruszsaccessories.gamemodifiers.configs;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.IAccessoryTooltip;
import com.mlib.config.DoubleConfig;
import com.mlib.math.Range;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoryPercent extends DoubleConfig implements IAccessoryTooltip {
	final float multiplier;

	public AccessoryPercent( double defaultValue, Range< Double > range, double multiplier ) {
		super( defaultValue, range );

		this.multiplier = ( float )multiplier;
	}

	public AccessoryPercent( double defaultValue, Range< Double > range ) {
		this( defaultValue, range, 1.0 );
	}

	public float getDefaultValue() {
		return this.asFloat();
	}

	public float getValue( AccessoryHandler handler ) {
		if( handler == null ) {
			return 0.0f;
		}

		return ( float )Mth.clamp( ( 1.0f + this.multiplier * handler.getBonus() ) * this.getDefaultValue(), this.range.from, this.range.to );
	}

	@Override
	public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		IAccessoryTooltip.build( key, DEFAULT_FORMAT )
			.addParameter( this.getPercentBonus( this::getDefaultValue, this::getValue, handler ) )
			.insertInto( components );
	}

	@Override
	public void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		if( Math.abs( this.getDefaultValue() - this.getValue( handler ) ) < 0.0001f ) {
			this.addTooltip( key, components, handler );
			return;
		}

		IAccessoryTooltip.build( key, DEFAULT_FORMAT )
			.addParameter( this.getPercentFormula( this::getDefaultValue, this::getValue, handler ) )
			.insertInto( components );
	}

	protected MutableComponent getPercentBonus( Supplier< Float > defaultBonus, Function< AccessoryHandler, Float > bonus, AccessoryHandler handler ) {
		MutableComponent component = new TextComponent( TextHelper.percent( bonus.apply( handler ) ) );
		float diff = bonus.apply( handler ) - defaultBonus.get();
		component.withStyle( Math.abs( diff ) >= 0.0001f ? handler.getBonusFormatting() : DEFAULT_FORMAT );

		return component;
	}

	protected MutableComponent getPercentFormula( Supplier< Float > defaultBonus, Function< AccessoryHandler, Float > bonus, AccessoryHandler handler ) {
		FormattedTranslatable component = new FormattedTranslatable( FORMULA_KEY, DEFAULT_FORMAT );
		component.addParameter( TextHelper.percent( defaultBonus.get() ) );
		float diff = bonus.apply( handler ) - defaultBonus.get();
		if( Math.abs( diff ) >= 0.0001f ) {
			component.addParameter( TextHelper.signedPercent( diff ), handler.getBonusFormatting() );
		}

		return component.create();
	}
}
