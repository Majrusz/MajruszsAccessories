package com.majruszsaccessories.accessories;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnItemDamaged;
import com.majruszlibrary.events.base.Priority;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.MiningDurabilityBonus;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.events.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class ToolScraps extends AccessoryHandler {
	public ToolScraps() {
		super( MajruszsAccessories.TOOL_SCRAPS, ToolScraps.class );

		this.add( MiningDurabilityBonus.create( 0.1f ) )
			.add( MiningDropChance.create() )
			.add( TradeOffer.create( MajruszsAccessories.GAMBLING_CARD, 1 ) );
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
				.addCondition( data->data.player != null )
				.addCondition( CustomConditions.dropChance( data->this.multiplier * data.itemStack.getMaxDamage(), data->data.player ) );

			handler.getConfig()
				.define( "durability_drop_chance_multiplier", Reader.number(), s->this.multiplier, ( s, v )->this.multiplier = Range.CHANCE.clamp( v ) );
		}

		private void spawnScraps( OnItemDamaged data ) {
			AnyPos pos = AnyPos.from( data.player.position() );

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
