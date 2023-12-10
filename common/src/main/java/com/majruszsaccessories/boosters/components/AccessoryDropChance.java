package com.majruszsaccessories.boosters.components;

import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.events.OnAccessoryDropChanceGet;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.tooltip.TooltipHelper;

public class AccessoryDropChance extends BonusComponent< BoosterItem > {
	RangedFloat multiplier = new RangedFloat().id( "accessory_drop_chance_multiplier" );

	public static ISupplier< BoosterItem > create( float multiplier ) {
		return handler->new AccessoryDropChance( handler, multiplier );
	}

	protected AccessoryDropChance( BonusHandler< BoosterItem > handler, float multiplier ) {
		super( handler );

		this.multiplier.set( multiplier, Range.of( 0.0f, 10.0f ) );

		OnAccessoryDropChanceGet.listen( this::increaseChance )
			.addCondition( CustomConditions.hasBooster( this::getItem, data->data.player ) );

		this.addTooltip( "majruszsaccessories.boosters.drop_chance", TooltipHelper.asBooster( this::getItem ), TooltipHelper.asFixedPercent( this.multiplier ) );

		this.multiplier.define( handler.getConfig() );
	}

	private void increaseChance( OnAccessoryDropChanceGet data ) {
		data.chance *= 1.0f + this.multiplier.get();
	}
}
