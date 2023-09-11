package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.MoreChestLoot;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.modhelper.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class AdventurerGuide extends AccessoryBase {
	public AdventurerGuide() {
		super( Registries.ADVENTURER_GUIDE );

		this.name( "AdventurerGuide" )
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
			chance.name( "spawn_chance" ).comment( "Chance for Adventurer Guide to spawn in any chest." );

			MoreChestLoot.OnChestOpened.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( chance, data->data.entity ) )
				.insertTo( group );
		}
	}
}