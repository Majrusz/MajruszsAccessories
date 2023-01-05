package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.ReduceDamageDealt;
import com.majruszsaccessories.gamemodifiers.list.ReduceDamageReceived;
import com.mlib.Utility;
import com.mlib.annotations.AutoInstance;
import com.mlib.blocks.BlockHelper;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.contexts.OnPlayerInteract;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class WhiteFlagItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "white_flag" );

	public WhiteFlagItem() {
		super( ID );
	}

	@AutoInstance
	public static class Register {
		public Register() {
			GameModifier.addNewGroup( SERVER_CONFIG, ID ).name( "WhiteFlag" );

			new ReduceDamageReceived( Registries.WHITE_FLAG, ID );
			new ReduceDamageDealt( Registries.WHITE_FLAG, ID );
			new SwingBehavior( Registries.WHITE_FLAG, ID );
			new AddToVillageChests( Registries.WHITE_FLAG, ID );
			new TradeOffer( Registries.WHITE_FLAG, ID );
		}
	}

	static class SwingBehavior extends AccessoryModifier {
		public SwingBehavior( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new OnPlayerInteract.Context( this::swing )
				.addCondition( data->data.itemStack.getItem() instanceof WhiteFlagItem )
				.addCondition( data->!data.player.getCooldowns().isOnCooldown( data.itemStack.getItem() ) )
				.insertTo( this );
		}

		private void swing( OnPlayerInteract.Data data ) {
			data.player.swing( data.hand );
			data.player.getCooldowns().addCooldown( data.itemStack.getItem(), Utility.secondsToTicks( 0.5 ) );
		}
	}

	static class AddToVillageChests extends AccessoryModifier {
		public AddToVillageChests( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey );

			new OnLoot.Context( this::addToGeneratedLoot )
				.addCondition( new Condition.IsServer<>() )
				.addCondition( new SpawnChance( 0.15 ) )
				.addCondition( new IsVillageChest() )
				.addCondition( OnLoot.HAS_ORIGIN )
				.addCondition( data->BlockHelper.getBlockEntity( data.level, data.origin ) instanceof RandomizableContainerBlockEntity )
				.addCondition( data->data.entity instanceof ServerPlayer )
				.insertTo( this );
		}

		static class SpawnChance extends Condition.Chance< OnLoot.Data > {
			public SpawnChance( double chance ) {
				super( chance );

				this.chance.name( "spawn_chance" ).comment( "Chance for White Flag to spawn in any village chest." );
			}
		}

		static class IsVillageChest extends OnLoot.Is {
			public IsVillageChest() {
				super(
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
				);

				this.ids.comment( "Determines which chests should contain White Flag." );
			}
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.LIBRARIAN, 5 );
		}
	}
}
