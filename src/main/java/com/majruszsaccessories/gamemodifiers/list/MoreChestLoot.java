package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.gamemodifiers.IAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.blocks.BlockHelper;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import com.mlib.gamemodifiers.data.OnLootData;
import com.mlib.levels.LevelHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MoreChestLoot extends AccessoryModifier {
	static final int BLOCKS_DISTANCE = 6000;
	final ChestChance sizeMultiplier = new ChestChance( "chest_size_bonus", "Extra multiplier for number of items acquired from chests.", false, 1.2, 0.0, 10.0 );

	public MoreChestLoot( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnLootContext onLoot = new OnLootContext( this.toAccessoryConsumer( this::increaseLoot ) );
		onLoot.addCondition( new Condition.IsServer() )
			.addCondition( OnLootContext.HAS_ORIGIN )
			.addCondition( data->BlockHelper.getBlockEntity( data.level, data.origin ) instanceof RandomizableContainerBlockEntity )
			.addCondition( data->data.entity instanceof ServerPlayer )
			.addConfig( this.sizeMultiplier );

		this.addContext( onLoot );
		this.addTooltip( this.sizeMultiplier, "majruszsaccessories.bonuses.more_chest_loot" );
	}

	private void increaseLoot( OnLootData data, AccessoryHandler handler ) {
		boolean hasIncreasedLoot = false;
		float sizeMultiplier = this.getFinalSizeMultiplier( ( ServerPlayer )data.entity, handler );
		for( ItemStack itemStack : data.generatedLoot ) {
			int count = Math.min( Random.roundRandomly( sizeMultiplier * itemStack.getCount() ), itemStack.getMaxStackSize() );
			hasIncreasedLoot = hasIncreasedLoot || count > itemStack.getCount();
			itemStack.setCount( count );
		}
		if( hasIncreasedLoot ) {
			ParticleHandler.AWARD.spawn( data.level, data.origin, 16, 1.5 );
		}
	}

	private float getFinalSizeMultiplier( ServerPlayer player, AccessoryHandler handler ) {
		Pair< Vec3, ServerLevel > spawnData = LevelHelper.getSpawnData( player );
		float distanceMultiplier = ( float )Mth.clamp( spawnData.getFirst().distanceTo( player.position() ) / BLOCKS_DISTANCE, 0.0, 1.0 );

		return 1.0f + this.sizeMultiplier.getValue( handler ) * distanceMultiplier;
	}

	static class ChestChance extends AccessoryPercent {
		public ChestChance( String name, String comment, boolean worldRestartRequired, double defaultValue, double min, double max ) {
			super( name, comment, worldRestartRequired, defaultValue, min, max );
		}

		@Override
		public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
			this.addTooltip( this::getPercentBonus, key, components, handler );
		}

		@Override
		public void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
			this.addTooltip( this::getPercentFormula, key, components, handler );
		}

		private void addTooltip( ComponentBuilder builder, String key, List< Component > components, AccessoryHandler handler ) {
			int blocksPerPercent = Math.round( BLOCKS_DISTANCE / ( this.getDefaultValue() * 100.0f ) );
			IAccessoryTooltip.build( key, DEFAULT_FORMAT )
				.addParameter( builder.apply( ()->0.01f, _handler->( 1.0f + handler.getBonus() ) / 100.0f, handler ) )
				.addParameter( Component.literal( "" + blocksPerPercent ).withStyle( DEFAULT_FORMAT ) )
				.addParameter( builder.apply( this::getDefaultValue, this::getValue, handler ) )
				.insertInto( components );
		}

		interface ComponentBuilder {
			MutableComponent apply( Supplier< Float > defaultBonus, Function< AccessoryHandler, Float > bonus, AccessoryHandler handler );
		}
	}
}
