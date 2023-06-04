package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.ReduceDamageDealt;
import com.majruszsaccessories.accessories.components.ReduceDamageReceived;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.mlib.Utility;
import com.mlib.annotations.AutoInstance;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.StringListConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.contexts.OnPlayerInteract;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.function.Supplier;
import java.util.stream.Stream;

@AutoInstance
public class WhiteFlag extends AccessoryBase {
	public WhiteFlag() {
		super( Registries.WHITE_FLAG );

		this.name( "WhiteFlag" )
			.add( ReduceDamageReceived.create() )
			.add( ReduceDamageDealt.create() )
			.add( TradeOffer.create( VillagerProfession.LIBRARIAN, 5 ) )
			.add( SwingBehavior.create() )
			.add( AddToVillageChests.create() );
	}

	static class SwingBehavior extends AccessoryComponent {
		public static ISupplier create() {
			return SwingBehavior::new;
		}

		protected SwingBehavior( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			OnPlayerInteract.listen( this::swing )
				.addCondition( Condition.predicate( data->data.itemStack.getItem().equals( item ) ) )
				.addCondition( Condition.predicate( data->!data.player.getCooldowns().isOnCooldown( data.itemStack.getItem() ) ) )
				.insertTo( group );
		}

		private void swing( OnPlayerInteract.Data data ) {
			data.player.swing( data.hand );
			data.player.getCooldowns().addCooldown( data.itemStack.getItem(), Utility.secondsToTicks( 0.5 ) );
		}
	}

	static class AddToVillageChests extends AccessoryComponent {
		public static ISupplier create() {
			return AddToVillageChests::new;
		}

		protected AddToVillageChests( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.15, Range.CHANCE );
			chance.name( "spawn_chance" ).comment( "Chance for White Flag to spawn in any village chest." );

			StringListConfig villageIds = new StringListConfig( Stream.of(
				BuiltInLootTables.VILLAGE_WEAPONSMITH,
				BuiltInLootTables.VILLAGE_TOOLSMITH,
				BuiltInLootTables.VILLAGE_ARMORER,
				BuiltInLootTables.VILLAGE_ARMORER,
				BuiltInLootTables.VILLAGE_CARTOGRAPHER,
				BuiltInLootTables.VILLAGE_MASON,
				BuiltInLootTables.VILLAGE_SHEPHERD,
				BuiltInLootTables.VILLAGE_BUTCHER,
				BuiltInLootTables.VILLAGE_FLETCHER,
				BuiltInLootTables.VILLAGE_FISHER,
				BuiltInLootTables.VILLAGE_TANNERY,
				BuiltInLootTables.VILLAGE_TEMPLE,
				BuiltInLootTables.VILLAGE_DESERT_HOUSE,
				BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
				BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
				BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
				BuiltInLootTables.VILLAGE_SAVANNA_HOUSE
			).map( ResourceLocation::toString ).toArray( String[]::new ) );
			villageIds.name( "loot_table_ids" ).comment( "Determines which chests should contain White Flag." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.is( villageIds ) )
				.addCondition( OnLoot.hasOrigin() )
				.addCondition( Condition.predicate( data->BlockHelper.getBlockEntity( data.getLevel(), data.origin ) instanceof RandomizableContainerBlockEntity ) )
				.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) )
				.insertTo( group );
		}
	}
}
