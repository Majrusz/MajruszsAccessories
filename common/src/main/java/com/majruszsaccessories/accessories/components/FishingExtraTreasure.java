package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.emitter.ParticleEmitter;
import com.majruszlibrary.events.OnFishingExtraItemsGet;
import com.majruszlibrary.item.LootHelper;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class FishingExtraTreasure extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new FishingExtraTreasure( handler, chance );
	}

	protected FishingExtraTreasure( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnFishingExtraItemsGet.listen( this::addExtraTreasure );

		this.addTooltip( "majruszsaccessories.bonuses.extra_fishing_treasure", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "extra_fishing_treasure", this.chance::define );
	}

	private void addExtraTreasure( OnFishingExtraItemsGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() || !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		data.extraItems.addAll( LootHelper.getLootTable( BuiltInLootTables.FISHING_TREASURE ).getRandomItems( LootHelper.toGiftParams( data.player ) ) );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnFishingExtraItemsGet data, AccessoryHolder holder ) {
		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );

		holder.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
