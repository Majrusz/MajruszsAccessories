package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.contexts.OnDamaged;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class ReduceDamageDealt extends AccessoryModifier {
	final AccessoryPercent penalty;

	public ReduceDamageDealt( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.6 );
	}

	public ReduceDamageDealt( Supplier< ? extends AccessoryItem > item, String configKey, double penalty ) {
		super( item, configKey );

		this.penalty = new AccessoryPercent( penalty, new Range<>( 0.01, 0.99 ), -1.0 );

		new OnDamaged.Context( this.toAccessoryConsumer( this::reduceDamage, data->data.attacker ) )
			.addConfig( this.penalty.name( "damage_dealt_penalty" ).comment( "Ratio of damage ignored when attacking." ) )
			.insertTo( this );

		this.addTooltip( this.penalty, "majruszsaccessories.bonuses.reduce_damage_dealt" );
	}

	private void reduceDamage( OnDamaged.Data data, AccessoryHandler handler ) {
		data.event.setAmount( data.event.getAmount() * ( 1.0f - this.penalty.getValue( handler ) ) );
	}
}
