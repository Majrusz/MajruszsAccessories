package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.components.AccessoryComponent;
import com.majruszsaccessories.components.MoreChestLoot;
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
public class AdventurersGuide extends AccessoryBase {
	public AdventurersGuide() {
		super( Registries.ADVENTURERS_GUIDE );

		this.name( "AdventurersGuide" )
			.add( MoreChestLoot.create() )
			.add( DropChance.create() )
			.add( TradeOffer.create( VillagerProfession.CARTOGRAPHER, 5 ) );
	}

	static class DropChance extends AccessoryComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.025, Range.CHANCE );
			chance.name( "spawn_chance" ).comment( "Chance for Adventurer's Guide to spawn in any chest." );

			MoreChestLoot.OnChestOpened.listen( this::addToGeneratedLoot )
				.addCondition( Condition.chance( chance ) )
				.insertTo( group );
		}
	}
}
