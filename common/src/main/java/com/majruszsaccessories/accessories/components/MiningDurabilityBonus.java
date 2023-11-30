package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.events.OnItemDamaged;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
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
			.addCondition( CustomConditions.chance( this::getItem, data->data.player, holder->holder.apply( this.chance ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.free_durability_cost", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "mining_free_durability_use", this.chance::define );
	}

	private void decreaseDurabilityCost( OnItemDamaged data ) {
		data.damage = 0;
		this.spawnEffects( data );
	}

	private void spawnEffects( OnItemDamaged data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 3 )
			.sizeBased( data.player )
			.emit( data.getServerLevel() );
	}
}
