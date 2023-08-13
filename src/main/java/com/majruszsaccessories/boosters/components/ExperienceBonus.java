package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.Random;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnPickupXp;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class ExperienceBonus extends BoosterComponent {
	final DoubleConfig experienceExtraMultiplier;

	public static ISupplier create( double chance ) {
		return ( item, group )->new ExperienceBonus( item, group, chance );
	}

	protected ExperienceBonus( Supplier< BoosterItem > item, ConfigGroup group, double experienceExtraMultiplier ) {
		super( item );

		this.experienceExtraMultiplier = new DoubleConfig( experienceExtraMultiplier, new Range<>( 0.01, 10.0 ) );

		OnPickupXp.listen( this::increaseExperience )
			.addCondition( CustomConditions.hasBooster( item, data->data.player ) )
			.addConfig( this.experienceExtraMultiplier.name( "experience_extra_multiplier" ).comment( "Extra experience multiplier from all sources." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.boosters.experience_bonus", TooltipHelper.asItem( item ), TooltipHelper.asFixedPercent( this.experienceExtraMultiplier ) );
	}

	private void increaseExperience( OnPickupXp.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.item.get() );
		if( holder.isValid() ) {
			int experiencePoints = Random.round( this.experienceExtraMultiplier.get() * data.event.getOrb().getValue() );
			if( experiencePoints > 0 ) {
				data.player.giveExperiencePoints( experiencePoints );
			}
		}
	}
}
