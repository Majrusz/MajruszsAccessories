package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnDamaged;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class ReduceDamageReceived extends AccessoryComponent {
	final DoubleConfig reduction;

	public static ISupplier create( double reduction ) {
		return ( item, group )->new ReduceDamageReceived( item, group, reduction );
	}

	public static ISupplier create() {
		return create( 0.2 );
	}

	protected ReduceDamageReceived( Supplier< AccessoryItem > item, ConfigGroup group, double reduction ) {
		super( item );

		this.reduction = new DoubleConfig( reduction, new Range<>( 0.01, 0.99 ) );

		OnDamaged.listen( this::reduceDamage )
			.addCondition( CustomConditions.hasAccessory( item, data->data.target ) )
			.addConfig( this.reduction.name( "damage_received_reduction" ).comment( "Ratio of damage ignored while being attacked." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.reduce_damage_received", TooltipHelper.asPercent( this.reduction ) );
	}

	private void reduceDamage( OnDamaged.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.target, this.item.get() );
		data.event.setAmount( data.event.getAmount() * ( 1.0f - holder.apply( this.reduction ) ) );
	}
}
