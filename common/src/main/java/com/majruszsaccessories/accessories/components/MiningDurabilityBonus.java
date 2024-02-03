package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.events.OnItemDamaged;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;

public class MiningDurabilityBonus extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new MiningDurabilityBonus( handler, bonus );
	}

	protected MiningDurabilityBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.chance.set( bonus, Range.CHANCE );

		OnItemDamaged.listen( this::decreaseDurabilityCost )
			.addCondition( data->data.player != null );

		this.addTooltip( "majruszsaccessories.bonuses.free_durability_cost", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "mining_free_durability_use", this.chance::define );
	}

	private void decreaseDurabilityCost( OnItemDamaged data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		data.damage = 0;
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnItemDamaged data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 3 )
			.sizeBased( data.player )
			.emit( data.getServerLevel() );
	}
}
