package com.majruszsaccessories.gamemodifiers.configs;

import com.majruszsaccessories.AccessoryHandler;
import com.mlib.config.IntegerConfig;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoryInteger extends IntegerConfig implements IAccessoryTooltip {
	final int multiplier;

	public AccessoryInteger( String name, String comment, boolean worldRestartRequired, int defaultValue, int min, int max, int multiplier ) {
		super( name, comment, worldRestartRequired, defaultValue, min, max );
		this.multiplier = multiplier;
	}

	public AccessoryInteger( String name, String comment, boolean worldRestartRequired, int defaultValue, int min, int max ) {
		this( name, comment, worldRestartRequired, defaultValue, min, max, 1 );
	}

	public int getDefaultValue() {
		return this.get();
	}

	public int getValue( AccessoryHandler handler ) {
		if( handler != null ) {
			return Mth.clamp( Math.round( ( 1.0f + this.multiplier * handler.getBonus() ) * this.getDefaultValue() ), this.min, this.max );
		}

		return 0;
	}

	@Override
	public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		IAccessoryTooltip.build( key, DEFAULT_FORMAT )
			.addParameter( this.getBonus( this::getDefaultValue, this::getValue, handler ) )
			.insertInto( components );
	}

	@Override
	public void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
		IAccessoryTooltip.build( key, DEFAULT_FORMAT )
			.addParameter( this.getFormula( this::getDefaultValue, this::getValue, handler ) )
			.insertInto( components );
	}

	@Override
	public boolean areTooltipsIdentical( AccessoryHandler handler ) {
		return this.getDefaultValue() == this.getValue( handler );
	}

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
