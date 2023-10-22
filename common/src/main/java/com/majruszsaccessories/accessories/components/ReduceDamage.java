package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

public class ReduceDamage extends AccessoryComponent {
	RangedFloat reduction = new RangedFloat().id( "reduction" ).maxRange( Range.of( 0.0f, 1.0f ) );

	public static ISupplier< AccessoryItem > create( float reduction ) {
		return handler->new ReduceDamage( handler, reduction );
	}

	protected ReduceDamage( Handler< AccessoryItem > handler, float reduction ) {
		super( handler );

		this.reduction.set( reduction, Range.of( 0.0f, 1.0f ) );

		OnEntityPreDamaged.listen( this::reduceDamageDealt )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.attacker ) );

		OnEntityPreDamaged.listen( this::reduceDamageReceived )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.target ) );

		this.addTooltip( "majruszsaccessories.bonuses.reduce_damage", TooltipHelper.asPercent( this.reduction ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "damage_reduction", this.reduction::define );
	}

	private void reduceDamageDealt( OnEntityPreDamaged data ) {
		data.damage *= 1.0f - CustomConditions.getLastHolder().apply( this.reduction );
	}

	private void reduceDamageReceived( OnEntityPreDamaged data ) {
		data.damage *= 1.0f - CustomConditions.getLastHolder().apply( this.reduction );
	}
}
