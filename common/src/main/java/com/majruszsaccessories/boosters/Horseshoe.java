package com.majruszsaccessories.boosters;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.common.components.TradeOffer;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;

@AutoInstance
public class Horseshoe extends BoosterHandler {
	public Horseshoe() {
		super( MajruszsAccessories.HORSESHOE, Horseshoe.class );

		this.add( LuckBonus.create( 1 ) )
			.add( SkeletonHorsemanDropChance.create() )
			.add( TradeOffer.create( MajruszsAccessories.REVERSE_CARD, 1 ) );
	}

	static class SkeletonHorsemanDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.334f;

		public static ISupplier< BoosterItem > create() {
			return SkeletonHorsemanDropChance::new;
		}

		protected SkeletonHorsemanDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Skeleton skeleton && skeleton.getRootVehicle() instanceof SkeletonHorse );

			handler.getConfig()
				.define( "skeleton_horseman_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
