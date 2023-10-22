package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemFished;
import com.mlib.data.Serializable;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

@AutoInstance
public class AnglerTrophy extends AccessoryHandler {
	public AnglerTrophy() {
		super( MajruszsAccessories.ANGLER_TROPHY );

		this.add( FishingLuckBonus.create( 3 ) )
			.add( FishingDropChance.create() )
			.add( TradeOffer.create( VillagerProfession.FISHERMAN, 5 ) );
	}

	static class FishingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.015f;

		public static ISupplier< AccessoryItem > create() {
			return FishingDropChance::new;
		}

		protected FishingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnItemFished.listen( this::onFished )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->data.player ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "fishing_drop_chance", ()->this.chance, x->this.chance = Range.CHANCE.clamp( x ) );
		}

		private void onFished( OnItemFished data ) {
			this.spawnFlyingItem( data.getLevel(), data.hook.position(), data.player.position() );
		}
	}
}
