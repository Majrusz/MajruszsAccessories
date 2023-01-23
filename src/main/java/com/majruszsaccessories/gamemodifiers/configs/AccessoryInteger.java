package com.majruszsaccessories.gamemodifiers.configs;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.IAccessoryTooltip;
import com.mlib.config.IntegerConfig;
import com.mlib.math.Range;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoryInteger extends IntegerConfig implements IAccessoryTooltip {
	final int multiplier;

	public AccessoryInteger( int defaultValue, Range< Integer > range, int multiplier ) {
		super( defaultValue, range );

		this.multiplier = multiplier;
	}

	public AccessoryInteger( int defaultValue, Range< Integer > range ) {
		this( defaultValue, range, 1 );
	}

	public int getDefaultValue() {
		return this.get();
	}

	public int getValue( AccessoryHandler handler ) {
		if( handler == null ) {
			return 0;
		}

		return Mth.clamp( Math.round( ( 1.0f + this.multiplier * handler.getBonus() ) * this.getDefaultValue() ), this.range.from, this.range.to );
	}

	@Override
	public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		IAccessoryTooltip.build( key, DEFAULT_FORMAT )
			.addParameter( this.getBonus( this::getDefaultValue, this::getValue, handler ) )
			.insertInto( components );
	}

	@Override
	public void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		if( this.getDefaultValue() == this.getValue( handler ) ) {
			this.addTooltip( key, components, handler );
			return;
		}

		IAccessoryTooltip.build( key, DEFAULT_FORMAT )
			.addParameter( this.getFormula( this::getDefaultValue, this::getValue, handler ) )
			.insertInto( components );
	}

	protected MutableComponent getBonus( Supplier< Integer > defaultBonus, Function< AccessoryHandler, Integer > bonus, AccessoryHandler handler ) {
		MutableComponent component = new TextComponent( String.format( "%d", bonus.apply( handler ) ) );
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
