package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.events.OnBabySpawned;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
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

		handler.getConfig()
			.define( "breeding_twins", this.chance::define );
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
		AccessoryHolder.get( data.player )
			.getParticleEmitter()
			.count( 4 )
			.sizeBased( child )
			.emit( data.getServerLevel() );
	}
}
