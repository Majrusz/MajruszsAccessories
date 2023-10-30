package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.ITooltipProvider;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.annotation.AutoInstance;
import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.OnPlayerTicked;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.data.Serializable;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.level.BlockHelper;
import com.mlib.level.LevelHelper;
import com.mlib.math.Random;
import com.mlib.math.Range;
import com.mlib.text.TextHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.Optional;
import java.util.function.Consumer;

public class MoreChestLoot extends BonusComponent< AccessoryItem > {
	static final int BLOCKS_DISTANCE = 6000;
	RangedFloat sizeMultiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float sizeMultiplier ) {
		return handler->new MoreChestLoot( handler, sizeMultiplier );
	}

	protected MoreChestLoot( BonusHandler< AccessoryItem > handler, float sizeMultiplier ) {
		super( handler );

		this.sizeMultiplier.set( sizeMultiplier, Range.of( 0.0f, 10.0f ) );

		OnChestOpened.listen( this::addExtraLoot )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->( LivingEntity )data.entity ) );

		this.addTooltip( "majruszsaccessories.bonuses.more_chest_loot", this.getPerPercentInfo(), this.getPercentInfo(), this.getCurrentInfo() );

		Serializable config = handler.getConfig();
		config.defineCustom( "chest_size_bonus", this.sizeMultiplier::define );
	}

	private void addExtraLoot( OnLootGenerated data ) {
		ServerPlayer player = ( ServerPlayer )data.entity;
		float sizeMultiplier = 1.0f + CustomConditions.getLastHolder().apply( this.sizeMultiplier ) * MoreChestLoot.getDistanceBonus( player );
		boolean hasIncreasedLoot = false;
		for( ItemStack itemStack : data.generatedLoot ) {
			int count = Math.min( Random.round( sizeMultiplier * itemStack.getCount() ), itemStack.getMaxStackSize() );
			hasIncreasedLoot = hasIncreasedLoot || count > itemStack.getCount();
			itemStack.setCount( count );
		}

		if( hasIncreasedLoot ) {
			this.spawnEffects( data );
		}
	}

	private void spawnEffects( OnLootGenerated data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 24 )
			.offset( ParticleEmitter.offset( 0.4f ) )
			.position( data.origin )
			.emit( data.getServerLevel() );
	}

	private ITooltipProvider getPerPercentInfo() {
		return TooltipHelper.asPercent( this.sizeMultiplier ).valueMultiplier( 1.0f / BLOCKS_DISTANCE ).scale( 4 );
	}

	private ITooltipProvider getPercentInfo() {
		return TooltipHelper.asPercent( this.sizeMultiplier ).scale( 4 );
	}

	private ITooltipProvider getCurrentInfo() {
		return holder->TextHelper.literal( TextHelper.percent( holder.apply( this.sizeMultiplier ) * BonusInfo.CURRENT_BONUS, 4 ) );
	}

	private static float getDistanceBonus( ServerPlayer player ) {
		Optional< LevelHelper.SpawnPoint > spawnPoint = LevelHelper.getSpawnPoint( player );

		return spawnPoint.map( point->( float )Mth.clamp( ( double )Math.round( point.position.distanceTo( player.position() ) ) / BLOCKS_DISTANCE, 0.0, 1.0 ) )
			.orElse( 0.0f );
	}

	public static class OnChestOpened {
		public static Context< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
			return OnLootGenerated.listen( consumer )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.hasLevel() )
				.addCondition( data->data.origin != null )
				.addCondition( data->data.entity instanceof ServerPlayer )
				.addCondition( data->{
					return BlockHelper.getEntity( data.getLevel(), data.origin ) instanceof RandomizableContainerBlockEntity
						|| data.lootId.toString().contains( "chest" );
				} );
		}
	}

	public static class BonusInfo extends Serializable {
		static float CURRENT_BONUS = 0.0f;
		float bonus;

		public BonusInfo( float bonus ) {
			this.bonus = bonus;

			this.defineFloat( "bonus", ()->this.bonus, x->this.bonus = x );
		}

		public BonusInfo() {
			this( 0.0f );
		}

		@Override
		@OnlyIn( Dist.CLIENT )
		public void onClient() {
			CURRENT_BONUS = this.bonus;
		}
	}

	@AutoInstance
	public static class Notifier {
		public Notifier() {
			OnPlayerTicked.listen( this::sendUpdatedBonus )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.cooldown( 1.0f ) );
		}

		private void sendUpdatedBonus( OnPlayerTicked data ) {
			ServerPlayer player = ( ServerPlayer )data.player;

			MajruszsAccessories.MORE_CHEST_LOOT.sendToClient( player, new BonusInfo( MoreChestLoot.getDistanceBonus( player ) ) );
		}
	}
}
