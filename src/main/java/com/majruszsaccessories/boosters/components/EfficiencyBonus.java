package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryExtraBonusGet;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class EfficiencyBonus extends BoosterComponent {
	final DoubleConfig bonus;

	public static ISupplier create( double chance ) {
		return ( item, group )->new EfficiencyBonus( item, group, chance );
	}

	protected EfficiencyBonus( Supplier< BoosterItem > item, ConfigGroup group, double experienceExtraMultiplier ) {
		super( item );

		this.bonus = new DoubleConfig( experienceExtraMultiplier, new Range<>( 0.01, 1.0 ) );

		OnAccessoryExtraBonusGet.listen( this::increaseBonus )
			.addCondition( Condition.predicate( data->data.holder.hasBooster( this.item.get() ) ) )
			.addConfig( this.bonus.name( "efficiency_bonus" ).comment( "Gives extra efficiency bonus to the accessory on which it is used." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.boosters.efficiency_bonus", TooltipHelper.asItem( item ), TooltipHelper.asFixedPercent( this.bonus ) );
	}

	private void increaseBonus( OnAccessoryExtraBonusGet.Data data ) {
		data.value += this.bonus.asFloat();
	}
}
