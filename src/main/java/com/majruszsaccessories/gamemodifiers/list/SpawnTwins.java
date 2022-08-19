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

import java.util.function.Supplier;

public class SpawnTwins extends AccessoryModifier {
	final AccessoryPercent chance = new AccessoryPercent( "twins_chance", "Chance to spawn twins when breeding animals.", false, 0.25, 0.0, 1.0 );

	public SpawnTwins( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnBabySpawnContext onLoot = new OnBabySpawnContext( this::spawnTwins );
		onLoot.addCondition( new Condition.IsServer() ).addConfig( this.chance );

		this.addContext( onLoot );
		this.addTooltip( this.chance, "majruszsaccessories.bonuses.spawn_twins" );
	}

	private void spawnTwins( OnBabySpawnData data ) {
		AccessoryHandler handler = AccessoryHandler.tryToCreate( data.entity, this.item.get() );
		if( handler == null || !Random.tryChance( this.chance.getValue( handler ) ) ) {
			return;
		}

		ParticleHandler.AWARD.spawn( data.level, data.child.position(), 8 );
	}
}
