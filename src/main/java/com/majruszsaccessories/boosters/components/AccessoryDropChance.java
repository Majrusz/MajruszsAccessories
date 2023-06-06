package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryDropChance;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class AccessoryDropChance extends BoosterComponent {
	final DoubleConfig chanceExtraMultiplier;

	public static ISupplier create( double chance ) {
		return ( item, group )->new AccessoryDropChance( item, group, chance );
	}

	protected AccessoryDropChance( Supplier< BoosterItem > item, ConfigGroup group, double extraChanceMultiplier ) {
		super( item );

		this.chanceExtraMultiplier = new DoubleConfig( extraChanceMultiplier, new Range<>( 0.01, 10.0 ) );

		OnAccessoryDropChance.listen( this::increaseChance )
			.addCondition( CustomConditions.hasBooster( item, data->data.player ) )
			.addConfig( this.chanceExtraMultiplier.name( "extra_chance_multiplier" ).comment( "Extra chance multiplier to drop accessories." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.boosters.drop_chance", TooltipHelper.asItem( item ), TooltipHelper.asFixedPercent( this.chanceExtraMultiplier ) );
	}

	private void increaseChance( OnAccessoryDropChance.Data data ) {
		data.chance *= 1.0 + this.chanceExtraMultiplier.get();
	}
}
