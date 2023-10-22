package com.majruszsaccessories.boosters;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.components.LuckBonus;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.common.BoosterHandler;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;

@AutoInstance
public class Horseshoe extends BoosterHandler {
	public Horseshoe() {
		super( MajruszsAccessories.HORSESHOE );

		this.add( LuckBonus.create( 1 ) )
			.add( SkeletonHorsemanDropChance.create() );
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

			Serializable config = handler.getConfig();
			config.defineFloat( "skeleton_horseman_drop_chance", ()->this.chance, x->this.chance = x );
		}
	}
}
