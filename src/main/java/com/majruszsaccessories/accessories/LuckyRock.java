package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.ExtraStoneLoot;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class LuckyRock extends AccessoryBase {
	public LuckyRock() {
		super( Registries.LUCKY_ROCK );

		this.name( "LuckyRock" )
			.add( ExtraStoneLoot.create() )
			.add( TradeOffer.create( VillagerProfession.MASON, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.0005, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Lucky Rock to drop when mining stone." );

			ExtraStoneLoot.OnStoneMined.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( chance, data->data.entity ) )
				.insertTo( group );
		}
	}
}
