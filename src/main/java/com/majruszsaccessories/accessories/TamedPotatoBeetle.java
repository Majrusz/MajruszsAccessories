package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.AccessoryComponent;
import com.majruszsaccessories.components.DoubleCrops;
import com.majruszsaccessories.components.TradeOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

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

			DoubleConfig chance = new DoubleConfig( 0.002, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Tamed Potato Beetle to drop when harvesting." );

			DoubleCrops.OnHarvest.listen( this::addToGeneratedLoot )
				.addCondition( Condition.chance( chance ) )
				.insertTo( group );
		}
	}
}
