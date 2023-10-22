package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnEntitySwimSpeedMultiplierGet;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

public class SwimmingSpeedBonus extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new SwimmingSpeedBonus( handler, bonus );
	}

	protected SwimmingSpeedBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.multiplier.set( bonus, Range.of( 0.0f, 10.0f ) );

		OnEntitySwimSpeedMultiplierGet.listen( this::increaseSwimSpeed )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.entity ) );

		this.addTooltip( "majruszsaccessories.bonuses.swim_bonus", TooltipHelper.asPercent( this.multiplier ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "swim_speed", this.multiplier::define );
	}

	private void increaseSwimSpeed( OnEntitySwimSpeedMultiplierGet data ) {
		data.multiplier *= 1.0f + CustomConditions.getLastHolder().apply( this.multiplier );
	}
}
