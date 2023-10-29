package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningDurabilityBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemDamaged;
import com.mlib.contexts.base.Priority;
import com.mlib.data.Serializable;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;

@AutoInstance
public class ToolScraps extends AccessoryHandler {
	public ToolScraps() {
		super( MajruszsAccessories.TOOL_SCRAPS );

		this.add( MiningDurabilityBonus.create( 0.1f ) )
			.add( MiningDropChance.create() )
			.add( TradeOffer.create( 7 ) );
	}

	static class MiningDropChance extends BonusComponent< AccessoryItem > {
		float multiplier = 0.0003f;

		public static ISupplier< AccessoryItem > create() {
			return MiningDropChance::new;
		}

		protected MiningDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnItemDamaged.listen( this::spawnScraps )
				.priority( Priority.LOWEST )
				.addCondition( OnItemDamaged::isAboutToBroke )
				.addCondition( CustomConditions.dropChance( data->this.multiplier * data.itemStack.getMaxDamage(), data->data.player ) );

			Serializable config = handler.getConfig();
			config.defineFloat( "durability_drop_chance_multiplier", ()->this.multiplier, x->this.multiplier = Range.CHANCE.clamp( x ) );
		}

		private void spawnScraps( OnItemDamaged data ) {
			AnyPos pos = AnyPos.from( data.player.position() );

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
