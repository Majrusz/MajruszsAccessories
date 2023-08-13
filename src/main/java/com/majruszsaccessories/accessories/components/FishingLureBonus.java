package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnFishingTimeSet;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class FishingLureBonus extends AccessoryComponent {
	final DoubleConfig timeMultiplier;

	public static ISupplier create( double bonus ) {
		return ( item, group )->new FishingLureBonus( item, group, bonus );
	}

	public static ISupplier create() {
		return create( 0.4 );
	}

	protected FishingLureBonus( Supplier< AccessoryItem > item, ConfigGroup group, double bonus ) {
		super( item );

		this.timeMultiplier = new DoubleConfig( bonus, new Range<>( 0.01, 0.99 ) );

		OnFishingTimeSet.listen( this::decreaseFishingTime )
			.addCondition( OnFishingTimeSet.hasPlayer() )
			.addConfig( this.timeMultiplier.name( "time_multiplier" ).comment( "Time reduction multiplier when fishing." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_lure", TooltipHelper.asPercent( this.timeMultiplier ) );
	}

	private void decreaseFishingTime( OnFishingTimeSet.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.item.get() );
		float multiplier = holder.isValid() ? holder.apply( this.timeMultiplier ) : 0.0f;
		data.ticks = Math.round( data.ticks * ( 1.0f - multiplier ) );
	}
}
