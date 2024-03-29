package com.majruszsaccessories.boosters.components;

import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.events.OnAccessoryExtraBonusGet;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.tooltip.TooltipHelper;

public class EfficiencyBonus extends BonusComponent< BoosterItem > {
	RangedFloat bonus = new RangedFloat().id( "efficiency_bonus" );

	public static ISupplier< BoosterItem > create( float bonus ) {
		return handler->new EfficiencyBonus( handler, bonus );
	}

	protected EfficiencyBonus( BonusHandler< BoosterItem > handler, float bonus ) {
		super( handler );

		this.bonus.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnAccessoryExtraBonusGet.listen( this::increaseEfficiency )
			.addCondition( data->data.holder.has( this.getItem() ) );

		this.addTooltip( "majruszsaccessories.boosters.efficiency_bonus", TooltipHelper.asBooster( this::getItem ), TooltipHelper.asFixedPercent( this.bonus ) );

		this.bonus.define( handler.getConfig() );
	}

	private void increaseEfficiency( OnAccessoryExtraBonusGet data ) {
		data.bonus += this.bonus.get();
	}
}
