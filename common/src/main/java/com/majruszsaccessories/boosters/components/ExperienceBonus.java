package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.common.Handler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnExpOrbPickedUp;
import com.mlib.data.Serializable;
import com.mlib.math.Range;

public class ExperienceBonus extends BoosterComponent {
	RangedFloat bonus = new RangedFloat().id( "experience_bonus" );

	public static ISupplier< BoosterItem > create( float bonus ) {
		return handler->new ExperienceBonus( handler, bonus );
	}

	protected ExperienceBonus( Handler< BoosterItem > handler, float bonus ) {
		super( handler );

		this.bonus.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnExpOrbPickedUp.listen( this::increaseExperience )
			.addCondition( CustomConditions.hasBooster( this::getItem, data->data.player ) );

		this.addTooltip( "majruszsaccessories.boosters.experience_bonus", TooltipHelper.asItem( this::getItem ), TooltipHelper.asFixedPercent( this.bonus ) );

		this.bonus.define( handler.getConfig() );
	}

	private void increaseExperience( OnExpOrbPickedUp data ) {
		data.experience += data.original * this.bonus.get();
	}
}