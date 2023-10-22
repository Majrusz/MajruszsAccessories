package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnBabySpawned;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.world.entity.AgeableMob;

public class BreedingTwins extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new BreedingTwins( handler, chance );
	}

	protected BreedingTwins( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnBabySpawned.listen( this::spawnTwins )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( data->data.player != null )
			.addCondition( CustomConditions.chance( this::getItem, data->data.player, holder->holder.apply( this.chance ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.spawn_twins", TooltipHelper.asPercent( this.chance ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "breeding_twins", this.chance::define );
	}

	private void spawnTwins( OnBabySpawned data ) {
		AgeableMob child = data.parentA.getBreedOffspring( data.getServerLevel(), data.parentB );
		if( child == null ) {
			return;
		}

		child.setBaby( true );
		child.absMoveTo( data.parentA.getX(), data.parentA.getY(), data.parentA.getZ(), 0.0f, 0.0f );
		data.getLevel().addFreshEntity( child );
		this.spawnEffects( data, child );
	}

	private void spawnEffects( OnBabySpawned data, AgeableMob child ) {
		float width = data.parentA.getBbWidth();
		float height = data.parentA.getBbHeight();

		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( Math.round( 4 * ( 1.0f + width + height ) ) )
			.offset( ()->AnyPos.from( width, height, width ).mul( 0.5 ).vec3() )
			.emit( data.getServerLevel(), AnyPos.from( child.position() ).add( 0.0, height * 0.5, 0.0 ).vec3() );
	}
}
