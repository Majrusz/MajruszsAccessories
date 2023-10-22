package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnPlayerTicked;
import com.mlib.contexts.base.Condition;
import com.mlib.entity.AttributeHandler;
import com.mlib.math.Range;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LuckBonus extends BonusComponent< BoosterItem > {
	final AttributeHandler attribute;
	final RangedInteger bonus = new RangedInteger().id( "luck_bonus" );

	public static ISupplier< BoosterItem > create( int luck ) {
		return handler->new LuckBonus( handler, luck );
	}

	protected LuckBonus( BonusHandler< BoosterItem > handler, int luck ) {
		super( handler );

		this.attribute = new AttributeHandler( "%s_luck_bonus".formatted( handler.getId() ), ()->Attributes.LUCK, AttributeModifier.Operation.ADDITION );
		this.bonus.set( luck, Range.of( 1, 10 ) );

		OnPlayerTicked.listen( this::updateLuck )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.cooldown( 0.2f ) );

		this.addTooltip( "majruszsaccessories.boosters.luck_bonus", TooltipHelper.asItem( this::getItem ), TooltipHelper.asFixedValue( this.bonus ) );

		this.bonus.define( handler.getConfig() );
	}

	private void updateLuck( OnPlayerTicked data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.getItem() );
		int luck = holder.isValid() ? holder.apply( this.bonus ) : 0;

		this.attribute.setValue( luck ).apply( data.player );
	}
}
