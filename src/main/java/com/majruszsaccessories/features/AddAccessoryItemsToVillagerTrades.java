package com.majruszsaccessories.features;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.items.AccessoryItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
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
		TRADE_REGISTRIES.add( new TradeRegistry( VillagerProfession.CLERIC, Instances.SECRET_INGREDIENT_ITEM, 2 ) );
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

		public TradeRegistry( VillagerProfession profession, AccessoryItem accessoryItem, int tradeTier ) {
			this.profession = profession;
			this.item = accessoryItem;
			this.tradeTier = tradeTier;
		}

		/** Adds new accessory item trade if profession is valid. */
		public void registerIfValidProfession( VillagerTradesEvent event ) {
			if( !this.profession.equals( event.getType() ) )
				return;

			Int2ObjectMap< List< ITrade > > tradeLevels = event.getTrades();
			List< ITrade > trades = tradeLevels.get( this.tradeTier );
			trades.add( new AccessoryItemTrade( this.item ) );
		}
	}

	/** Villager trade with accessory item only. */
	static class AccessoryItemTrade implements ITrade {
		private final AccessoryItem tradeItem;

		public AccessoryItemTrade( AccessoryItem tradeItem ) {
			this.tradeItem = tradeItem;
		}

		@Override
		public MerchantOffer getOffer( Entity trader, Random rand ) {
			return new AccessoryItemOffer( this.tradeItem );
		}
	}

	/** Villager trade offer with accessory item. */
	static class AccessoryItemOffer extends MerchantOffer {
		protected AccessoryItem accessoryItem;

		public AccessoryItemOffer( AccessoryItem accessoryItem ) {
			super( new ItemStack( accessoryItem, 1 ), new ItemStack( Items.EMERALD, 17 ), 2, 40, 0.05f );

			this.accessoryItem = accessoryItem;
		}

		@Override
		public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
			return this.accessoryItem.equals( itemStack1.getItem() ) || this.accessoryItem.equals( itemStack2.getItem() );
		}
	}
}
