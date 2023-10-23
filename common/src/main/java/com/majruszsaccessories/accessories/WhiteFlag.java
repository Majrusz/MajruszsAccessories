package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.ReduceDamage;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.OnPlayerInteracted;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import com.mlib.platform.Side;
import com.mlib.time.TimeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;

@AutoInstance
public class WhiteFlag extends AccessoryHandler {
	public WhiteFlag() {
		super( MajruszsAccessories.WHITE_FLAG );

		this.add( ReduceDamage.create( 0.2f ) )
			.add( SwingBehavior.create() )
			.add( VillagerChestDropChance.create() )
			.add( TradeOffer.create( VillagerProfession.LIBRARIAN, 5 ) );
	}

	static class SwingBehavior extends BonusComponent< AccessoryItem > {
		public static ISupplier< AccessoryItem > create() {
			return SwingBehavior::new;
		}

		protected SwingBehavior( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnPlayerInteracted.listen( this::swing )
				.addCondition( data->data.itemStack.is( this.getItem() ) )
				.addCondition( data->!data.player.getCooldowns().isOnCooldown( this.getItem() ) );
		}

		private void swing( OnPlayerInteracted data ) {
			if( Side.isLogicalServer() ) {
				data.player.getCooldowns().addCooldown( data.itemStack.getItem(), TimeHelper.toTicks( 0.5 ) );
				data.cancelInteraction( InteractionResult.CONSUME );
			} else {
				data.player.swing( data.hand );
			}
		}
	}

	static class VillagerChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.025f;
		List< ResourceLocation > lootIds = List.of(
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

		public static ISupplier< AccessoryItem > create() {
			return VillagerChestDropChance::new;
		}

		protected VillagerChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( data->this.lootIds.contains( data.lootId ) )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.entity ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "village_chest_spawn_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
			config.defineLocation( "village_chest_ids", ()->this.lootIds, x->this.lootIds = x );
		}
	}
}
