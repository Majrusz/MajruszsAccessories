package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.blocks.BlockHelper;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleCrops extends AccessoryModifier {
	final AccessoryPercent chance;

	public DoubleCrops( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.25 );
	}

	public DoubleCrops( Supplier< ? extends AccessoryItem > item, String configKey, double chance ) {
		super( item, configKey );

		this.chance = new AccessoryPercent( chance, Range.CHANCE );

		new OnCropsContext( this.toAccessoryConsumer( this::doubleLoot, this.chance ) )
			.addConfig( this.chance.name( "double_crops_chance" ).comment( "Chance to double crops when harvesting." ) )
			.insertTo( this );

		this.addTooltip( this.chance, "majruszsaccessories.bonuses.double_crops" );
	}

	private void doubleLoot( OnLoot.Data data, AccessoryHandler handler ) {
		ParticleHandler.AWARD.spawn( data.level, data.origin, 6 );
		data.generatedLoot.addAll( new ArrayList<>( data.generatedLoot ) );
	}

	public static class OnCropsContext extends OnLoot.Context {
		public OnCropsContext( Consumer< OnLoot.Data > consumer ) {
			super( consumer );

			this.addCondition( new Condition.IsServer<>() )
				.addCondition( OnLoot.HAS_ORIGIN )
				.addCondition( data->data.blockState != null && BlockHelper.isCropAtMaxAge( data.blockState ) )
				.addCondition( data->data.entity instanceof LivingEntity );
		}
	}
}
