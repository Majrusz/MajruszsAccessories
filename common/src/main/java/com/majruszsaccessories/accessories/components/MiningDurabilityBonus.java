package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnItemDamaged;
import com.mlib.data.Serializable;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;

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

		Serializable config = handler.getConfig();
		config.defineCustom( "mining_free_durability_use", this.chance::define );
	}

	private void decreaseDurabilityCost( OnItemDamaged data ) {
		data.damage = 0;
		this.spawnEffects( data );
	}

	private void spawnEffects( OnItemDamaged data ) {
		float width = data.player.getBbWidth();
		float height = data.player.getBbHeight();

		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 3 )
			.offset( ()->AnyPos.from( width, height, width ).mul( 0.5 ).vec3() )
			.emit( data.getServerLevel(), AnyPos.from( data.player.position() ).add( 0.0f, 0.5f * height, 0.0f ).vec3() );
	}
}
