package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.tooltip.TooltipHelper;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.contexts.OnDamaged;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class ReduceDamageDealt extends AccessoryComponent {
	final DoubleConfig penalty;

	public static ISupplier create( double penalty ) {
		return ( item, group )->new ReduceDamageDealt( item, group, penalty );
	}

	public static ISupplier create() {
		return create( 0.6 );
	}

	protected ReduceDamageDealt( Supplier< AccessoryItem > item, ConfigGroup group, double penalty ) {
		super( item );

		this.penalty = new DoubleConfig( penalty, new Range<>( 0.01, 0.99 ) );

		OnDamaged.listen( this::reduceDamage )
			.addCondition( CustomConditions.hasAccessory( item, data->data.attacker ) )
			.addConfig( this.penalty.name( "damage_dealt_penalty" ).comment( "Ratio of damage ignored when attacking." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.reduce_damage_dealt", TooltipHelper.asPercent( this.penalty, -1.0 ) );
	}

	private void reduceDamage( OnDamaged.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.attacker, this.item.get() );
		data.event.setAmount( data.event.getAmount() * ( 1.0f - holder.apply( this.penalty, -1.0 ) ) );
	}
}
