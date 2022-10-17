package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.contexts.OnDamaged;

import java.util.function.Supplier;

public class ReduceDamageDealt extends AccessoryModifier {
	final AccessoryPercent penalty = new AccessoryPercent( "damage_dealt_penalty", "Ratio of damage ignored when attacking.", false, 0.6, 0.01, 0.99, -1.0 );

	public ReduceDamageDealt( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnDamaged.Context onDamaged = new OnDamaged.Context( this.toAccessoryConsumer( this::reduceDamage, data->data.attacker ) );
		onDamaged.addConfig( this.penalty );

		this.addContext( onDamaged );
		this.addTooltip( this.penalty, "majruszsaccessories.bonuses.reduce_damage_dealt" );
	}

	private void reduceDamage( OnDamaged.Data data, AccessoryHandler handler ) {
		data.event.setAmount( data.event.getAmount() * ( 1.0f - this.penalty.getValue( handler ) ) );
	}
}
