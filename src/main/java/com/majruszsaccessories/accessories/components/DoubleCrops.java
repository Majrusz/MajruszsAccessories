package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.effects.ParticleHandler;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleCrops extends AccessoryComponent {
	final DoubleConfig chance;

	public static AccessoryComponent.ISupplier create( double chance ) {
		return ( item, group )->new DoubleCrops( item, group, chance );
	}

	public static AccessoryComponent.ISupplier create() {
		return create( 0.25 );
	}

	protected DoubleCrops( Supplier< AccessoryItem > item, ConfigGroup group, double chance ) {
		super( item );

		this.chance = new DoubleConfig( chance, Range.CHANCE );
		this.chance.name( "double_crops_chance" ).comment( "Chance to double crops when harvesting." );

		OnHarvest.listen( this::doubleLoot )
			.addCondition( CustomConditions.chance( this.item, data->( LivingEntity )data.entity, holder->holder.apply( this.chance ) ) )
			.addConfig( this.chance )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.double_crops", TooltipHelper.asPercent( this.chance ) );
	}

	private void doubleLoot( OnLoot.Data data ) {
		ParticleHandler.AWARD.spawn( data.getServerLevel(), data.origin, 6 );
		data.generatedLoot.addAll( new ArrayList<>( data.generatedLoot ) );
	}

	public static class OnHarvest {
		public static Context< OnLoot.Data > listen( Consumer< OnLoot.Data > consumer ) {
			return OnLoot.listen( consumer )
				.addCondition( Condition.isServer() )
				.addCondition( OnLoot.hasOrigin() )
				.addCondition( Condition.predicate( data->data.blockState != null && BlockHelper.isCropAtMaxAge( data.blockState ) ) )
				.addCondition( Condition.predicate( data->data.entity instanceof LivingEntity ) );
		}
	}
}
