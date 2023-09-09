package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.modhelper.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnItemFished;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class AnglerTrophy extends AccessoryBase {
	public AnglerTrophy() {
		super( Registries.ANGLER_TROPHY );

		this.name( "AnglerTrophy" )
			.add( FishingLuckBonus.create() )
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
			chance.name( "drop_chance" ).comment( "Chance to drop Angler Trophy when fishing." );

			OnItemFished.listen( this::onFished )
				.addCondition( CustomConditions.dropChance( chance, data->data.player ) )
				.insertTo( group );
		}

		private void onFished( OnItemFished.Data data ) {
			this.spawnFlyingItem( data.getLevel(), data.hook.position(), data.player.position() );
		}
	}
}
