package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnPlayerTick;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;

import java.util.function.Supplier;

public class SwimmingSpeedBonus extends AccessoryComponent {
	final AttributeHandler attribute;
	final DoubleConfig speedMultiplier;

	public static ISupplier create( double bonus ) {
		return ( item, group )->new SwimmingSpeedBonus( item, group, bonus );
	}

	public static ISupplier create() {
		return create( 0.2 );
	}

	protected SwimmingSpeedBonus( Supplier< AccessoryItem > item, ConfigGroup group, double bonus ) {
		super( item );

		this.attribute = new AttributeHandler( "%sSwimmingBonus".formatted( group.getName() ), ForgeMod.SWIM_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL );
		this.speedMultiplier = new DoubleConfig( bonus, new Range<>( 0.01, 10.0 ) );

		OnPlayerTick.listen( this::updateSpeed )
			.addCondition( Condition.< OnPlayerTick.Data > cooldown( 4, Dist.DEDICATED_SERVER ).configurable( false ) )
			.addConfig( this.speedMultiplier.name( "swim_speed_multiplier" ).comment( "Extra speed multiplier when swimming." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.swim_bonus", TooltipHelper.asPercent( this.speedMultiplier ) );
	}

	private void updateSpeed( OnPlayerTick.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.item.get() );
		double multiplier = holder.isValid() ? holder.apply( this.speedMultiplier ) : 0.0;

		this.attribute.setValue( multiplier ).apply( data.player );
	}
}
