package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnItemFished;
import com.mlib.contexts.OnPlayerTicked;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.entity.AttributeHandler;
import com.mlib.level.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class FishingLuckBonus extends BonusComponent< AccessoryItem > {
	final AttributeHandler attribute;
	RangedInteger luck = new RangedInteger().id( "bonus" ).maxRange( Range.of( 1, 100 ) );

	public static ISupplier< AccessoryItem > create( int luck ) {
		return handler->new FishingLuckBonus( handler, luck );
	}

	protected FishingLuckBonus( BonusHandler< AccessoryItem > handler, int luck ) {
		super( handler );

		this.attribute = new AttributeHandler( "%s_fishing_luck_bonus".formatted( handler.getId() ), ()->Attributes.LUCK, AttributeModifier.Operation.ADDITION );
		this.luck.set( luck, Range.of( 1, 10 ) );

		OnPlayerTicked.listen( this::updateLuck )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.cooldown( 4 ) );

		OnItemFished.listen( this::spawnEffects )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.player ) );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_luck", TooltipHelper.asValue( this.luck ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "fishing_luck", this.luck::define );
	}

	private void updateLuck( OnPlayerTicked data ) {
		this.attribute.setValue( this.getLuck( data.player ) ).apply( data.player );
	}

	private int getLuck( Player player ) {
		if( player.fishing == null ) {
			return 0;
		}

		AccessoryHolder holder = AccessoryHolder.find( player, this.getItem() );
		return holder.isValid() ? holder.apply( this.luck ) : 0;
	}

	private void spawnEffects( OnItemFished data ) {
		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );

		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.emit( data.getServerLevel(), AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() );
	}
}
