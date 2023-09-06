package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnFishingTimeSet;
import com.mlib.contexts.OnFoodPropertiesGet;
import com.mlib.math.Range;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

public class SaturationBonus extends AccessoryComponent {
	final DoubleConfig multiplier;

	public static ISupplier create( double bonus ) {
		return ( item, group )->new SaturationBonus( item, group, bonus );
	}

	public static ISupplier create() {
		return create( 0.5 );
	}

	protected SaturationBonus( Supplier< AccessoryItem > item, ConfigGroup group, double bonus ) {
		super( item );

		this.multiplier = new DoubleConfig( bonus, new Range<>( 0.01, 10.0 ) );

		OnFoodPropertiesGet.listen( this::decreaseFishingTime )
			.addCondition( CustomConditions.hasAccessory( item, data->data.entity ) )
			.addConfig( this.multiplier.name( "saturation_multiplier" ).comment( "Extra food saturation multiplier for all edible items." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.saturation_bonus", TooltipHelper.asPercent( this.multiplier ) );
	}

	private void decreaseFishingTime( OnFoodPropertiesGet.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.entity, this.item.get() );
		FoodProperties.Builder builder = new FoodProperties.Builder();
		builder.nutrition( data.properties.getNutrition() );
		builder.saturationMod( data.properties.getSaturationModifier() * ( 1.0f + holder.apply( this.multiplier ) ) );
		if( data.properties.isMeat() ) {
			builder.meat();
		}
		if( data.properties.canAlwaysEat() ) {
			builder.alwaysEat();
		}
		if( data.properties.isFastFood() ) {
			builder.fast();
		}

		data.properties = builder.build();
	}
}
