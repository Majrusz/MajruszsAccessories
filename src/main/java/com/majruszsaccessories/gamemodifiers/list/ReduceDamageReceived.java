package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.contexts.OnDamaged;

import java.util.function.Supplier;

public class ReduceDamageReceived extends AccessoryModifier {
	final AccessoryPercent reduction = new AccessoryPercent( "damage_received_reduction", "Ratio of damage ignored while being attacked.", false, 0.2, 0.01, 0.99 );

	public ReduceDamageReceived( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnDamaged.Context onDamaged = new OnDamaged.Context( this.toAccessoryConsumer( this::reduceDamage, data->data.target ) );
		onDamaged.addConfig( this.reduction );

		this.addContext( onDamaged );
		this.addTooltip( this.reduction, "majruszsaccessories.bonuses.reduce_damage_received" );
	}

	private void reduceDamage( OnDamaged.Data data, AccessoryHandler handler ) {
		data.event.setAmount( data.event.getAmount() * ( 1.0f - this.reduction.getValue( handler ) ) );
	}
}
