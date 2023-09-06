package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.boosters.BoosterBase;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.accessories.components.LowerSpawnRate;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnLoot;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.world.entity.monster.Endermite;

import java.util.function.Supplier;

@AutoInstance
public class PeaceEmblem extends AccessoryBase {
	public PeaceEmblem() {
		super( Registries.PEACE_EMBLEM );

		this.name( "PeaceEmblem" )
			.add( LowerSpawnRate.create( 0.1 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.05, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Peace Emblem to drop from Endermite." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Endermite ) )
				.insertTo( group );
		}
	}
}
