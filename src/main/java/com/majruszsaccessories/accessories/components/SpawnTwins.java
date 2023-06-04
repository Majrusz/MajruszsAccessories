package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.tooltip.TooltipHelper;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.contexts.OnBabySpawn;
import com.mlib.math.Range;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpawnTwins extends AccessoryComponent {
	final DoubleConfig chance;

	public static AccessoryComponent.ISupplier create( double chance ) {
		return ( item, group )->new SpawnTwins( item, group, chance );
	}

	public static AccessoryComponent.ISupplier create() {
		return create( 0.25 );
	}

	protected SpawnTwins( Supplier< AccessoryItem > item, ConfigGroup group, double chance ) {
		super( item );

		this.chance = new DoubleConfig( chance, Range.CHANCE );

		OnTwinsSpawn.listen( this::spawnTwins )
			.addCondition( CustomConditions.chance( item, data->data.player, holder->holder.apply( this.chance ) ) )
			.addConfig( this.chance.name( "twins_chance" ).comment( "Chance to spawn twins when breeding animals." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.spawn_twins", TooltipHelper.asPercent( this.chance ) );
	}

	private void spawnTwins( OnBabySpawn.Data data ) {
		Animal parentA = ( Animal )data.parentA, parentB = ( Animal )data.parentB;
		AgeableMob child = parentA.getBreedOffspring( data.getServerLevel(), parentB );
		if( child == null ) {
			return;
		}

		child.setBaby( true );
		child.absMoveTo( parentA.getX(), parentA.getY(), parentA.getZ(), 0.0f, 0.0f );
		data.getLevel().addFreshEntity( child );
		ParticleHandler.AWARD.spawn( data.getServerLevel(), child.position().add( 0.0, 0.5, 0.0 ), 8, ParticleHandler.offset( 2.0f ) );
	}

	public static class OnTwinsSpawn {
		public static Context< OnBabySpawn.Data > listen( Consumer< OnBabySpawn.Data > consumer ) {
			return OnBabySpawn.listen( consumer )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.predicate( data->data.parentA instanceof Animal ) )
				.addCondition( Condition.predicate( data->data.parentB instanceof Animal ) );
		}
	}
}
