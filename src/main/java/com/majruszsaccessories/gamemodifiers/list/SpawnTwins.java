package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnBabySpawn;
import com.mlib.math.Range;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpawnTwins extends AccessoryModifier {
	final AccessoryPercent chance;

	public SpawnTwins( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.25 );
	}

	public SpawnTwins( Supplier< ? extends AccessoryItem > item, String configKey, double chance ) {
		super( item, configKey );

		this.chance = new AccessoryPercent( chance, Range.CHANCE );

		new OnTwinsSpawnContext( this.toAccessoryConsumer( this::spawnTwins, this.chance ) )
			.addConfig( this.chance.name( "twins_chance" ).comment( "Chance to spawn twins when breeding animals." ) )
			.insertTo( this );

		this.addTooltip( this.chance, "majruszsaccessories.bonuses.spawn_twins" );
	}

	private void spawnTwins( OnBabySpawn.Data data, AccessoryHandler handler ) {
		assert data.level != null;
		Animal parentA = ( Animal )data.parentA, parentB = ( Animal )data.parentB;
		AgeableMob child = parentA.getBreedOffspring( data.level, parentB );
		if( child == null ) {
			return;
		}

		child.setBaby( true );
		child.absMoveTo( parentA.getX(), parentA.getY(), parentA.getZ(), 0.0f, 0.0f );
		data.level.addFreshEntity( child );
		ParticleHandler.AWARD.spawn( data.level, child.position().add( 0.0, 0.5, 0.0 ), 8, ParticleHandler.offset( 2.0f ) );
	}

	public static class OnTwinsSpawnContext extends OnBabySpawn.Context {
		public OnTwinsSpawnContext( Consumer< OnBabySpawn.Data > consumer ) {
			super( consumer );

			this.addCondition( new Condition.IsServer<>() )
				.addCondition( data->data.parentA instanceof Animal )
				.addCondition( data->data.parentB instanceof Animal );
		}
	}
}
