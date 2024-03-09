package com.majruszsaccessories.boosters.components;

import com.majruszlibrary.entity.AttributeHandler;
import com.majruszlibrary.events.OnPlayerTicked;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LuckBonus extends BonusComponent< BoosterItem > {
	final AttributeHandler attribute;
	final RangedFloat bonus = new RangedFloat().id( "luck_bonus" );

	public static ISupplier< BoosterItem > create( float luck ) {
		return handler->new LuckBonus( handler, luck );
	}

	protected LuckBonus( BonusHandler< BoosterItem > handler, float luck ) {
		super( handler );

		this.attribute = new AttributeHandler( "%s_luck_bonus".formatted( handler.getId() ), ()->Attributes.LUCK, AttributeModifier.Operation.ADDITION );
		this.bonus.set( luck, Range.of( 0.0f, 10.0f ) );

		OnPlayerTicked.listen( this::updateLuck )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.cooldown( 0.2f ) );

		this.addTooltip( "majruszsaccessories.boosters.luck_bonus", TooltipHelper.asBooster( this::getItem ), TooltipHelper.asFixedValue( this.bonus ) );

		this.bonus.define( handler.getConfig() );
	}

	private void updateLuck( OnPlayerTicked data ) {
		this.attribute.setValue( this.bonus.get() * AccessoryHolders.get( data.player ).getBoostersCount( this::getItem ) ).apply( data.player );
	}
}
