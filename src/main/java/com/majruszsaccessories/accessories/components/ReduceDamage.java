package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnDamaged;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class ReduceDamage extends AccessoryComponent {
	final DoubleConfig reduction;

	public static ISupplier create( double reduction ) {
		return ( item, group )->new ReduceDamage( item, group, reduction );
	}

	public static ISupplier create() {
		return create( 0.2 );
	}

	protected ReduceDamage( Supplier< AccessoryItem > item, ConfigGroup group, double reduction ) {
		super( item );

		this.reduction = new DoubleConfig( reduction, new Range<>( 0.01, 0.99 ) );

		OnDamaged.listen( this::reduceDamageDealt )
			.addCondition( CustomConditions.hasAccessory( item, data->data.attacker ) )
			.insertTo( group );

		OnDamaged.listen( this::reduceDamageReceived )
			.addCondition( CustomConditions.hasAccessory( item, data->data.target ) )
			.insertTo( group );

		group.addConfig( this.reduction.name( "damage_reduction" ).comment( "Ratio of damage ignored when dealing and receiving damage." ) );

		this.addTooltip( "majruszsaccessories.bonuses.reduce_damage", TooltipHelper.asPercent( this.reduction ) );
	}

	private void reduceDamageDealt( OnDamaged.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.target, this.item.get() );
		data.event.setAmount( data.event.getAmount() * ( 1.0f - holder.apply( this.reduction ) ) );
	}

	private void reduceDamageReceived( OnDamaged.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.attacker, this.item.get() );
		data.event.setAmount( data.event.getAmount() * ( 1.0f - holder.apply( this.reduction ) ) );
	}
}
