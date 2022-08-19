package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnBabySpawnContext;
import com.mlib.gamemodifiers.data.OnBabySpawnData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

import java.util.function.Supplier;

public class SpawnTwins extends AccessoryModifier {
	final AccessoryPercent chance = new AccessoryPercent( "twins_chance", "Chance to spawn twins when breeding animals.", false, 0.25, 0.0, 1.0 );

	public SpawnTwins( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnBabySpawnContext onLoot = new OnBabySpawnContext( this::spawnTwins );
		onLoot.addCondition( new Condition.IsServer() )
			.addCondition( data->data.parentA instanceof Animal )
			.addCondition( data->data.parentB instanceof Animal )
			.addConfig( this.chance );

		this.addContext( onLoot );
		this.addTooltip( this.chance, "majruszsaccessories.bonuses.spawn_twins" );
	}

	private void spawnTwins( OnBabySpawnData data ) {
		assert data.level != null;
		AccessoryHandler handler = AccessoryHandler.tryToCreate( data.player, this.item.get() );
		if( handler == null || !Random.tryChance( this.chance.getValue( handler ) ) ) {
			return;
		}

		Animal parentA = ( Animal )data.parentA, parentB = ( Animal )data.parentB;
		AgeableMob child = parentA.getBreedOffspring( data.level, parentB );
		if( child == null ) {
			return;
		}

		child.setBaby( true );
		child.absMoveTo( parentA.getX(), parentA.getY(), parentA.getZ(), 0.0f, 0.0f );
		data.level.addFreshEntity( child );
		ParticleHandler.AWARD.spawn( data.level, child.position(), 8 );
	}
}
