package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.contexts.OnDamaged;

import java.util.function.Supplier;

public class ReduceDamageReceived extends AccessoryModifier {
	final AccessoryPercent reduction;

	public ReduceDamageReceived( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.2 );
	}

	public ReduceDamageReceived( Supplier< ? extends AccessoryItem > item, String configKey, double reduction ) {
		super( item, configKey, "", "" );
		this.reduction = new AccessoryPercent( "damage_received_reduction", "Ratio of damage ignored while being attacked.", false, reduction, 0.01, 0.99 );

		OnDamaged.Context onDamaged = new OnDamaged.Context( this.toAccessoryConsumer( this::reduceDamage, data->data.target ) );
		onDamaged.addConfig( this.reduction );

		this.addContext( onDamaged );
		this.addTooltip( this.reduction, "majruszsaccessories.bonuses.reduce_damage_received" );
	}

	private void reduceDamage( OnDamaged.Data data, AccessoryHandler handler ) {
		data.event.setAmount( data.event.getAmount() * ( 1.0f - this.reduction.getValue( handler ) ) );
	}
}
