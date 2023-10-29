package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemFished;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import com.mlib.registry.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Stream;

@AutoInstance
public class AnglerTrophy extends AccessoryHandler {
	public AnglerTrophy() {
		super( MajruszsAccessories.ANGLER_TROPHY );

		this.add( FishingLuckBonus.create( 3 ) )
			.add( FishingDropChance.create( 0.01f ) )
			.add( TradeOffer.create( 7 ) );
	}

	public static class FishingDropChance extends BonusComponent< AccessoryItem > {
		float fishingChance = 0.005f;

		public static ISupplier< AccessoryItem > create( float chance ) {
			return handler->new FishingDropChance( handler, chance );
		}

		protected FishingDropChance( BonusHandler< AccessoryItem > handler, float chance ) {
			super( handler );

			this.fishingChance = chance;

			OnItemFished.listen( this::onFished )
				.addCondition( CustomConditions.dropChance( ()->this.fishingChance, data->data.player ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "fishing_drop_chance", ()->this.fishingChance, x->this.fishingChance = Range.CHANCE.clamp( x ) );
		}

		private void onFished( OnItemFished data ) {
			this.spawnFlyingItem( data.getLevel(), data.hook.position(), data.player.position() );
		}
	}

	public static class FishDropChance extends BonusComponent< AccessoryItem > {
		float fishChance;
		List< ResourceLocation > lootIds = Stream.of(
			"minecraft:cod",
			"minecraft:pufferfish",
			"minecraft:salmon",
			"minecraft:tropical_fish"
		).map( ResourceLocation::new ).toList();

		public static ISupplier< AccessoryItem > create( float chance ) {
			return handler->new FishDropChance( handler, chance );
		}

		protected FishDropChance( BonusHandler< AccessoryItem > handler, float chance ) {
			super( handler );

			this.fishChance = chance;

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( data->data.entity != null )
				.addCondition( data->this.lootIds.contains( Registries.get( data.entity.getType() ) ) )
				.addCondition( CustomConditions.dropChance( ()->this.fishChance, data->data.killer ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "fish_drop_chance", ()->this.fishChance, x->this.fishChance = Range.CHANCE.clamp( x ) );
			config.defineLocation( "fish_ids", ()->this.lootIds, x->this.lootIds = x );
		}
	}
}
