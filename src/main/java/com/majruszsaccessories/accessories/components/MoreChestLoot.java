package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.tooltip.ITooltipProvider;
import com.majruszsaccessories.accessories.tooltip.TooltipHelper;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import com.mlib.math.Range;
import com.mlib.text.TextHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MoreChestLoot extends AccessoryComponent {
	static final int BLOCKS_DISTANCE = 6000;
	final DoubleConfig sizeMultiplier;

	public static ISupplier create( double sizeMultiplier ) {
		return ( item, group )->new MoreChestLoot( item, group, sizeMultiplier );
	}

	public static ISupplier create() {
		return create( 1.2 );
	}

	protected MoreChestLoot( Supplier< AccessoryItem > item, ConfigGroup group, double sizeMultiplier ) {
		super( item );

		this.sizeMultiplier = new DoubleConfig( sizeMultiplier, new Range<>( 0.0, 10.0 ) );
		this.sizeMultiplier.name( "chest_size_bonus" ).comment( "Extra multiplier for number of items acquired from chests." );

		OnChestOpened.listen( this::increaseLoot )
			.addCondition( CustomConditions.has( this.item, data->( LivingEntity )data.entity ) )
			.addConfig( this.sizeMultiplier )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.more_chest_loot", this.getPercentInfo(), this.getBlockInfo(), TooltipHelper.asPercent( this.sizeMultiplier ) );
	}

	private void increaseLoot( OnLoot.Data data ) {
		boolean hasIncreasedLoot = false;
		float sizeMultiplier = this.getFinalSizeMultiplier( ( ServerPlayer )data.entity );
		for( ItemStack itemStack : data.generatedLoot ) {
			int count = Math.min( Random.roundRandomly( sizeMultiplier * itemStack.getCount() ), itemStack.getMaxStackSize() );
			hasIncreasedLoot = hasIncreasedLoot || count > itemStack.getCount();
			itemStack.setCount( count );
		}
		if( hasIncreasedLoot ) {
			ParticleHandler.AWARD.spawn( data.getServerLevel(), data.origin, 16, ParticleHandler.offset( 0.5f ) );
		}
	}

	private float getFinalSizeMultiplier( ServerPlayer player ) {
		AccessoryHolder holder = AccessoryHolder.find( player, this.item.get() );
		Pair< Vec3, ServerLevel > spawnData = LevelHelper.getSpawnData( player );
		float distanceMultiplier = ( float )Mth.clamp( spawnData.getFirst()
			.distanceTo( player.position() ) / BLOCKS_DISTANCE, 0.0, 1.0 );

		return 1.0f + holder.apply( this.sizeMultiplier ) * distanceMultiplier;
	}

	private ITooltipProvider getPercentInfo() {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				float bonus = holder.getBonus();

				return Component.translatable( TextHelper.percent( 0.01f + holder.getBonus() / 100.0f ) )
					.withStyle( bonus != 0.0f ? holder.getBonusFormatting() : TooltipHelper.DEFAULT_FORMAT );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				float bonus = holder.getBonus();

				return TooltipHelper.asFormula( TextHelper.percent( 0.01f ), Component.literal( TextHelper.signedPercent( holder.getBonus() / 100.0f ) )
					.withStyle( bonus != 0.0f ? holder.getBonusFormatting() : TooltipHelper.DEFAULT_FORMAT ) );
			}
		};
	}

	private ITooltipProvider getBlockInfo() {
		return holder->Component.translatable( "" + Math.round( BLOCKS_DISTANCE / ( this.sizeMultiplier.get() * 100.0f ) ) );
	}

	public static class OnChestOpened {
		public static Context< OnLoot.Data > listen( Consumer< OnLoot.Data > consumer ) {
			return OnLoot.listen( consumer )
				.addCondition( Condition.isServer() )
				.addCondition( OnLoot.hasOrigin() )
				.addCondition( Condition.predicate( data->BlockHelper.getBlockEntity( data.getLevel(), data.origin ) instanceof RandomizableContainerBlockEntity ) )
				.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) );
		}
	}
}
