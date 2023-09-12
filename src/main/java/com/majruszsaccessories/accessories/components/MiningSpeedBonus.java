package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnBreakSpeed;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class MiningSpeedBonus extends AccessoryComponent {
	final DoubleConfig speedMultiplier;

	public static ISupplier create( double bonus ) {
		return ( item, group )->new MiningSpeedBonus( item, group, bonus );
	}

	public static ISupplier create() {
		return create( 0.1 );
	}

	protected MiningSpeedBonus( Supplier< AccessoryItem > item, ConfigGroup group, double bonus ) {
		super( item );

		this.speedMultiplier = new DoubleConfig( bonus, new Range<>( 0.01, 10.0 ) );

		OnBreakSpeed.listen( this::increaseMineSpeed )
			.addCondition( CustomConditions.hasAccessory( item, data->data.player ) )
			.addConfig( this.speedMultiplier.name( "mine_speed_multiplier" ).comment( "Extra speed multiplier when mining blocks." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.mine_bonus", TooltipHelper.asPercent( this.speedMultiplier ) );
	}

	private void increaseMineSpeed( OnBreakSpeed.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.item.get() );
		double multiplier = holder.isValid() ? holder.apply( this.speedMultiplier ) : 0.0;

		data.newSpeed += data.originalSpeed * multiplier;
	}
}
