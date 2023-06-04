package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.DoubleCrops;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Priority;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Predicate;
import java.util.function.Supplier;

@AutoInstance
public class TamedPotatoBeetle extends AccessoryBase {
	public TamedPotatoBeetle() {
		super( Registries.TAMED_POTATO_BEETLE );

		this.name( "TamedPotatoBeetle" )
			.add( DoubleCrops.create() )
			.add( TradeOffer.create( VillagerProfession.FARMER, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleCrops.OnHarvest.listen( this::addToGeneratedLoot )
				.addCondition( DropChance.chance() )
				.insertTo( group );
		}

		private static Condition< OnLoot.Data > chance() {
			DoubleConfig chance = new DoubleConfig( 0.002, Range.CHANCE );
			DoubleConfig chanceMultiplier = new DoubleConfig( 2.5, new Range<>( 1.0, 10.0 ) );
			Predicate< OnLoot.Data > predicate = data->{
				double finalChance = chance.getOrDefault();
				if( data.blockState != null && data.blockState.getBlock().equals( Blocks.POTATOES ) ) {
					finalChance *= chanceMultiplier.getOrDefault();
				}

				return Random.tryChance( finalChance );
			};

			return Condition.predicate( predicate )
				.priority( Priority.HIGH )
				.configurable( true )
				.addConfig( chance.name( "drop_chance" ).comment( "Chance for Tamed Potato Beetle to drop when harvesting." ) )
				.addConfig( chanceMultiplier.name( "potato_multiplier" ).comment( "Chance multiplier when harvesting potatoes." ) );
		}
	}
}
