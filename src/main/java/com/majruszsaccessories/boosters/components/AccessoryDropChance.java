package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.accessories.tooltip.TooltipHelper;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.boosters.BoosterItem;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.contexts.OnDamaged;
import com.mlib.math.Range;

import java.util.function.Supplier;

public class AccessoryDropChance extends BoosterComponent {
	final DoubleConfig chance;

	public static ISupplier create( double chance ) {
		return ( item, group )->new AccessoryDropChance( item, group, chance );
	}

	protected AccessoryDropChance( Supplier< BoosterItem > item, ConfigGroup group, double chance ) {
		super( item );

		this.chance = new DoubleConfig( chance, Range.CHANCE );
		this.chance.name( "double_crops_chance" ).comment( "Chance to double crops when harvesting." );

		OnDamaged.listen( this::reduceDamage )
			.addCondition( CustomConditions.hasBooster( item, data->data.target ) )
			.addConfig( this.chance )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.boosters.drop_chance", TooltipHelper.asItem( item ), TooltipHelper.asFixedPercent( this.chance ) );
	}

	private void reduceDamage( OnDamaged.Data data ) {
		// AccessoryHolder holder = AccessoryHolder.find( data.target, this.item.get() );
		// data.event.setAmount( data.event.getAmount() * ( 1.0f - holder.apply( this.reduction ) ) );
	}
}
