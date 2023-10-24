package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.OnAccessoryExtraBonusGet;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.tooltip.TooltipHelper;

public class EfficiencyNegation extends BonusComponent< BoosterItem > {
	public static ISupplier< BoosterItem > create() {
		return EfficiencyNegation::new;
	}

	protected EfficiencyNegation( BonusHandler< BoosterItem > handler ) {
		super( handler );

		OnAccessoryExtraBonusGet.listen( this::negateEfficiency )
			.addCondition( data->data.holder.hasBooster( this.getItem() ) );

		this.addTooltip( "majruszsaccessories.boosters.efficiency_negation", TooltipHelper.asItem( this::getItem ) );
	}

	private void negateEfficiency( OnAccessoryExtraBonusGet data ) {
		data.bonus += -2.0f * data.holder.getBaseBonus();
	}
}
