package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.AccessoryComponent;
import com.majruszsaccessories.components.SpawnTwins;
import com.majruszsaccessories.components.TradeOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnBabySpawn;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class IdolOfFertility extends AccessoryBase {
	public IdolOfFertility() {
		super( Registries.IDOL_OF_FERTILITY );

		this.name( "IdolOfFertility" )
			.add( SpawnTwins.create() )
			.add( TradeOffer.create( VillagerProfession.BUTCHER, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static AccessoryComponent.ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.01, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Idol of Fertility to drop when breeding animals." );

			SpawnTwins.OnTwinsSpawn.listen( this::spawnTotem )
				.addCondition( Condition.chance( chance ) )
				.insertTo( group );
		}

		private void spawnTotem( OnBabySpawn.Data data ) {
			this.spawnFlyingItem( data.getLevel(), data.parentA.position(), data.parentB.position() );
		}
	}
}
