package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.ITooltipProvider;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.Random;
import com.mlib.blocks.BlockHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnLoot;
import com.mlib.contexts.OnPlayerTick;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.data.SerializableStructure;
import com.mlib.effects.ParticleHandler;
import com.mlib.levels.LevelHelper;
import com.mlib.math.Range;
import com.mlib.modhelper.AutoInstance;
import com.mlib.text.TextHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

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
		this.sizeMultiplier.name( "chest_size_bonus" ).comment( "Extra multiplier for loot amount acquired from chests." );

		OnChestOpened.listen( this::increaseLoot )
			.addCondition( CustomConditions.hasAccessory( this.item, data->( LivingEntity )data.entity ) )
			.addConfig( this.sizeMultiplier )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.more_chest_loot", this.getPercentInfo(), TooltipHelper.asPercent( this.sizeMultiplier ), this.getCurrentInfo() );
	}

	private void increaseLoot( OnLoot.Data data ) {
		boolean hasIncreasedLoot = false;
		float sizeMultiplier = this.getFinalSizeMultiplier( ( ServerPlayer )data.entity );
		for( ItemStack itemStack : data.generatedLoot ) {
			int count = Math.min( Random.round( sizeMultiplier * itemStack.getCount() ), itemStack.getMaxStackSize() );
			hasIncreasedLoot = hasIncreasedLoot || count > itemStack.getCount();
			itemStack.setCount( count );
		}
		if( hasIncreasedLoot ) {
			ParticleHandler.AWARD.spawn( data.getServerLevel(), data.origin, 16, ParticleHandler.offset( 0.25f ) );
		}
	}

	private float getFinalSizeMultiplier( ServerPlayer player ) {
		return 1.0f + AccessoryHolder.find( player, this.item.get() ).apply( this.sizeMultiplier ) * getDistanceBonus( player );
	}

	private ITooltipProvider getPercentInfo() {
		return TooltipHelper.asPercent( this.sizeMultiplier ).valueMultiplier( 1.0f / BLOCKS_DISTANCE ).scale( 4 );
	}

	private ITooltipProvider getCurrentInfo() {
		return holder->Component.literal( TextHelper.percent( holder.apply( this.sizeMultiplier ) * BonusInfo.CURRENT_BONUS ) );
	}

	private static float getDistanceBonus( ServerPlayer player ) {
		Pair< Vec3, ServerLevel > spawnData = LevelHelper.getSpawnData( player );

		return ( float )Mth.clamp( spawnData.getFirst().distanceTo( player.position() ) / BLOCKS_DISTANCE, 0.0, 1.0 );
	}

	public static class OnChestOpened {
		public static Context< OnLoot.Data > listen( Consumer< OnLoot.Data > consumer ) {
			return OnLoot.listen( consumer )
				.addCondition( Condition.isServer() )
				.addCondition( OnLoot.hasOrigin() )
				.addCondition( Condition.predicate( data->{
					return BlockHelper.getBlockEntity( data.getLevel(), data.origin ) instanceof RandomizableContainerBlockEntity
						|| data.context.getQueriedLootTableId().toString().contains( "chest" );
				} ) )
				.addCondition( Condition.predicate( data->data.entity instanceof ServerPlayer ) );
		}
	}

	public static class BonusInfo extends SerializableStructure {
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
		public void onClient( NetworkEvent.Context context ) {
			super.onClient( context );

			CURRENT_BONUS = this.bonus;
		}
	}

	@AutoInstance
	public static class Notifier {
		public Notifier() {
			OnPlayerTick.listen( this::sendUpdatedBonus )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.cooldown( 1.0, Dist.DEDICATED_SERVER ) );
		}

		private void sendUpdatedBonus( OnPlayerTick.Data data ) {
			ServerPlayer player = ( ServerPlayer )data.player;

			Registries.HELPER.sendMessage( PacketDistributor.PLAYER.with( ()->player ), new BonusInfo( getDistanceBonus( player ) ) );
		}
	}
}
