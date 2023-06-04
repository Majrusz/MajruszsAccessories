package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.contexts.OnItemFished;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class AnglersTrophy extends AccessoryBase {
	public AnglersTrophy() {
		super( Registries.ANGLERS_TROPHY );

		this.name( "AnglersTrophy" )
			.add( FishingLuckBonus.create() )
			.add( FishingLureBonus.create() )
			.add( TradeOffer.create( VillagerProfession.FISHERMAN, 5 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends AccessoryComponent {
		public static AccessoryComponent.ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< AccessoryItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.015, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance to drop Angler's Trophy when fishing." );

			OnItemFished.listen( this::onFished )
				.addCondition( CustomConditions.dropChance( chance, data->data.player ) )
				.insertTo( group );
		}

		private void onFished( OnItemFished.Data data ) {
			this.spawnFlyingItem( data.getLevel(), data.hook.position(), data.player.position() );
		}
	}
}
