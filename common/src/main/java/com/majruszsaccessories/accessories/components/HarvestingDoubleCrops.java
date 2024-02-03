package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.level.BlockHelper;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HarvestingDoubleCrops extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new HarvestingDoubleCrops( handler, chance );
	}

	protected HarvestingDoubleCrops( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnCropHarvested.listen( this::doubleLoot );

		this.addTooltip( "majruszsaccessories.bonuses.double_crops", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "double_crops", this.chance::define );
	}

	private void doubleLoot( OnLootGenerated data ) {
		AccessoryHolder holder = AccessoryHolders.get( ( LivingEntity )data.entity ).get( this::getItem );
		if( !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		data.generatedLoot.addAll( new ArrayList<>( data.generatedLoot ) );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnLootGenerated data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 5 )
			.position( data.origin )
			.emit( data.getServerLevel() );
	}

	public static class OnCropHarvested {
		public static Event< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
			return OnLootGenerated.listen( consumer )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->data.origin != null )
				.addCondition( data->data.blockState != null && BlockHelper.isCropAtMaxAge( data.blockState ) )
				.addCondition( data->data.entity instanceof LivingEntity );
		}
	}
}
