package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.emitter.ParticleEmitter;
import com.majruszlibrary.entity.AttributeHandler;
import com.majruszlibrary.events.OnItemFished;
import com.majruszlibrary.events.OnPlayerTicked;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class FishingLuckBonus extends BonusComponent< AccessoryItem > {
	final AttributeHandler attribute;
	RangedFloat luck = new RangedFloat().id( "bonus" ).maxRange( Range.of( 0.0f, 100.0f ) );

	public static ISupplier< AccessoryItem > create( float luck ) {
		return handler->new FishingLuckBonus( handler, luck );
	}

	protected FishingLuckBonus( BonusHandler< AccessoryItem > handler, float luck ) {
		super( handler );

		this.attribute = new AttributeHandler( "%s_fishing_luck_bonus".formatted( handler.getId() ), ()->Attributes.LUCK, AttributeModifier.Operation.ADDITION );
		this.luck.set( luck, Range.of( 0.0f, 10.0f ) );

		OnPlayerTicked.listen( this::updateLuck )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.cooldown( 4 ) );

		OnItemFished.listen( this::spawnEffects )
			.addCondition( Condition.isLogicalServer() );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_luck", TooltipHelper.asValue( this.luck ) );

		handler.getConfig()
			.define( "fishing_luck", this.luck::define );
	}

	private void updateLuck( OnPlayerTicked data ) {
		this.attribute.setValue( this.getLuck( data.player ) ).apply( data.player );
	}

	private float getLuck( Player player ) {
		if( player.fishing == null ) {
			return 0.0f;
		}

		AccessoryHolder holder = AccessoryHolders.get( player ).get( this::getItem );
		return holder.isValid() && !holder.isBonusDisabled() ? holder.apply( this.luck ) : 0.0f;
	}

	private void spawnEffects( OnItemFished data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );
		holder.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
