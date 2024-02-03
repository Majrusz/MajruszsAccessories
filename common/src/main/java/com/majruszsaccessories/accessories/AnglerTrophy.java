package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnItemFished;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.math.Range;
import com.majruszlibrary.registry.Registries;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Stream;

@AutoInstance
public class AnglerTrophy extends AccessoryHandler {
	public AnglerTrophy() {
		super( MajruszsAccessories.ANGLER_TROPHY, AnglerTrophy.class );

		this.add( FishingLuckBonus.create( 3.0f ) )
			.add( FishingDropChance.create( 0.01f ) )
			.add( TradeOffer.create() );
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
				.addCondition( CustomConditions.dropChance( s->this.fishingChance, data->data.player ) );

			handler.getConfig()
				.define( "fishing_drop_chance", Reader.number(), s->this.fishingChance, ( s, v )->this.fishingChance = Range.CHANCE.clamp( v ) );
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
				.addCondition( data->this.lootIds.contains( Registries.ENTITY_TYPES.getId( data.entity.getType() ) ) )
				.addCondition( CustomConditions.dropChance( s->this.fishChance, data->data.killer ) );

			handler.getConfig()
				.define( "fish_drop_chance", Reader.number(), s->this.fishChance, ( s, v )->this.fishChance = Range.CHANCE.clamp( v ) )
				.define( "fish_ids", Reader.list( Reader.location() ), s->this.lootIds, ( s, v )->this.lootIds = v );
		}
	}
}
