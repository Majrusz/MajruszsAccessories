package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.BaseOffer;
import com.majruszsaccessories.gamemodifiers.list.ReduceDamageDealt;
import com.majruszsaccessories.gamemodifiers.list.ReduceDamageReceived;
import com.mlib.Utility;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.contexts.OnPlayerInteract;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class WhiteFlagItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "white_flag" );
	static final ConfigGroup GROUP = SERVER_CONFIG.addGroup( GameModifier.addNewGroup( ID, "WhiteFlag", "" ) );

	public static Supplier< WhiteFlagItem > create() {
		GameModifiersHolder< WhiteFlagItem > holder = AccessoryItem.newHolder( ID, WhiteFlagItem::new );
		holder.addModifier( ReduceDamageReceived::new );
		holder.addModifier( ReduceDamageDealt::new );
		holder.addModifier( SwingBehavior::new );
		holder.addModifier( AddToVillageChests::new );
		holder.addModifier( TradeOffer::new );

		return holder::getRegistry;
	}

	static class SwingBehavior extends AccessoryModifier {
		public SwingBehavior( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnPlayerInteract.Context onPlayerInteract = new OnPlayerInteract.Context( this::swing );
			onPlayerInteract.addCondition( data->data.itemStack.getItem() instanceof WhiteFlagItem )
				.addCondition( data->!data.player.getCooldowns().isOnCooldown( data.itemStack.getItem() ) );

			this.addContext( onPlayerInteract );
		}

		private void swing( OnPlayerInteract.Data data ) {
			data.player.swing( data.hand );
			data.player.getCooldowns().addCooldown( data.itemStack.getItem(), Utility.secondsToTicks( 0.5 ) );
		}
	}

	static class AddToVillageChests extends AccessoryModifier {
		public AddToVillageChests( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLoot.Context onLoot = new OnLoot.Context( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.15, "spawn_chance", "Chance for White Flag to spawn in any village chest." ) )
				.addCondition( OnLoot.HAS_ORIGIN )
				.addCondition( data->BlockHelper.getBlockEntity( data.level, data.origin ) instanceof RandomizableContainerBlockEntity )
				.addCondition( data->data.entity instanceof ServerPlayer )
				.addCondition( data->data.context.getQueriedLootTableId().toString().contains( "minecraft:chests/village" ) );

			this.addContext( onLoot );
		}
	}

	static class TradeOffer extends BaseOffer {
		public TradeOffer( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, VillagerProfession.LIBRARIAN, 5 );
		}
	}
}
