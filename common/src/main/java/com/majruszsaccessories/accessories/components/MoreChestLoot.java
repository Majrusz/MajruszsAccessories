package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.emitter.ParticleEmitter;
import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.OnPlayerTicked;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszlibrary.platform.Side;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.ITooltipProvider;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

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
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->OnChestOpened.findPlayer( data ).orElse( null ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.more_chest_loot", this.getPerPercentInfo(), this.getPercentInfo(), this.getCurrentInfo() );

		handler.getConfig()
			.define( "chest_size_bonus", this.sizeMultiplier::define );
	}

	private void addExtraLoot( OnLootGenerated data ) {
		ServerPlayer player = OnChestOpened.findPlayer( data ).orElseThrow();
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
		public static Event< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
			return OnLootGenerated.listen( consumer )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->data.lootId.toString().contains( "chests/" ) );
		}

		public static Optional< ServerPlayer > findPlayer( OnLootGenerated data ) {
			if( data.entity instanceof ServerPlayer player ) {
				return Optional.of( player );
			}

			if( data.origin != null && data.level.getNearestPlayer( data.origin.x, data.origin.y, data.origin.z, 5.0f, true ) instanceof ServerPlayer player ) {
				return Optional.of( player );
			}

			return Optional.empty();
		}
	}

	public static class BonusInfo {
		static {
			Serializables.get( BonusInfo.class )
				.define( "bonus", Reader.number(), s->s.bonus, ( s, v )->s.bonus = v );

			Side.runOnClient( ()->()->MajruszsAccessories.MORE_CHEST_LOOT.addClientCallback( BonusInfo::onClient ) );
		}

		static float CURRENT_BONUS = 0.0f;
		float bonus;

		public BonusInfo( float bonus ) {
			this.bonus = bonus;
		}

		public BonusInfo() {
			this( 0.0f );
		}

		@OnlyIn( Dist.CLIENT )
		private static void onClient( BonusInfo data ) {
			CURRENT_BONUS = data.bonus;
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
