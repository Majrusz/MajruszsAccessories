package com.majruszsaccessories.features;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.items.AccessoryItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Adds all accessory items to villager trades. */
@Mod.EventBusSubscriber
public class AddAccessoryItemsToVillagerTrades {
	private static final List< TradeRegistry > TRADE_REGISTRIES = new ArrayList<>();

	static {
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.BUTCHER, Instances.IDOL_OF_FERTILITY_ITEM, 2 ) );
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.FARMER, Instances.GIANT_SEED_ITEM, 5 ) );
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.FISHERMAN, Instances.FISHERMAN_EMBLEM_ITEM, 2 ) );
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.MASON, Instances.LUCKY_ROCK_ITEM, 2 ) );
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.SHEPHERD, Instances.TAMING_CERTIFICATE_ITEM, 2 ) );
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.CLERIC, Instances.SECRET_INGREDIENT_ITEM, 2, 7 ) );
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.CLERIC, Instances.MAGMA_STONE_ITEM, 3 ) );
	}

	@SubscribeEvent
	public static void addTrades( VillagerTradesEvent event ) {
		for( TradeRegistry tradeRegistry : TRADE_REGISTRIES )
			tradeRegistry.registerIfValidProfession( event );
	}

	/** Class for easier trade registry. */
	static class TradeRegistry {
		private final VillagerProfession profession;
		private final AccessoryItem item;
		private final int tradeTier;
		private final int amountOfEmeralds;

		public TradeRegistry( VillagerProfession profession, AccessoryItem accessoryItem, int tradeTier, int amountOfEmeralds ) {
			this.profession = profession;
			this.item = accessoryItem;
			this.tradeTier = tradeTier;
			this.amountOfEmeralds = amountOfEmeralds;
		}

		public TradeRegistry( VillagerProfession profession, AccessoryItem accessoryItem, int tradeTier ) {
			this( profession, accessoryItem, tradeTier, 17 );
		}

		/** Adds new accessory item trade if profession is valid. */
		public void registerIfValidProfession( VillagerTradesEvent event ) {
			if( !this.profession.equals( event.getType() ) )
				return;

			Int2ObjectMap< List< VillagerTrades.ItemListing > > tradeLevels = event.getTrades();
			List< VillagerTrades.ItemListing > trades = tradeLevels.get( this.tradeTier );
			trades.add( new AccessoryItemTrade( this.item, this.amountOfEmeralds ) );
		}
	}

	/** Villager trade with accessory item only. */
	static class AccessoryItemTrade implements VillagerTrades.ItemListing {
		private final AccessoryItem tradeItem;
		private final int amountOfEmeralds;

		public AccessoryItemTrade( AccessoryItem tradeItem, int amountOfEmeralds ) {
			this.tradeItem = tradeItem;
			this.amountOfEmeralds = amountOfEmeralds;
		}

		@Override
		public MerchantOffer getOffer( Entity trader, Random rand ) {
			return new AccessoryItemOffer( this.tradeItem, this.amountOfEmeralds );
		}
	}

	/** Villager trade offer with accessory item. */
	static class AccessoryItemOffer extends MerchantOffer {
		protected AccessoryItem accessoryItem;

		public AccessoryItemOffer( AccessoryItem accessoryItem, int amountOfEmeralds ) {
			super( new ItemStack( accessoryItem, 1 ), new ItemStack( Items.EMERALD, amountOfEmeralds ), 2, 40, 0.05f );

			this.accessoryItem = accessoryItem;
		}

		@Override
		public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
			return this.accessoryItem.equals( itemStack1.getItem() ) || this.accessoryItem.equals( itemStack2.getItem() );
		}
	}
}
