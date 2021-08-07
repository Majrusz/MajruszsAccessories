package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.MajruszLibrary;
import com.mlib.Random;
import com.mlib.TimeConverter;
import com.mlib.config.DoubleConfig;
import com.mlib.entities.EntityHelper;
import com.mlib.events.AnyLootModificationEvent;
import com.mlib.loot_modifiers.SmeltingItems;
import com.mlib.particles.ParticleHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

/** Stone that makes player always inflict on fire on enemies and automatically smelt blocks. */
@Mod.EventBusSubscriber
public class MagmaStoneItem extends AccessoryItem {
	static {
		SmeltingItems.registerList.add( new SmeltingItems.Register() {
			@Override
			public boolean shouldBeExecuted( ServerLevel serverLevel, Player player, @Nullable ItemStack itemStack, BlockState blockState ) {
				return Instances.MAGMA_STONE_ITEM.hasAny( player );
			}
		} );
	}

	protected final DoubleConfig dropChance;
	protected final IntegrationDoubleConfig fireDuration;

	public MagmaStoneItem() {
		super( "Magma Stone", "magma_stone" );

		String dropComment = "Chance for Magma Stone to drop from Magma Cube.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.001, 0.0, 1.0 );

		String fireComment = "Duration of fire inflicted to enemies. (in seconds)";
		this.fireDuration = new IntegrationDoubleConfig( "fire_duration", fireComment, 5.0, 7.0, 9.0, 1.0, 60.0 );

		this.group.addConfigs( this.dropChance, this.fireDuration );
	}

	@SubscribeEvent
	public static void onHurt( LivingHurtEvent event ) {
		MagmaStoneItem magmaStone = Instances.MAGMA_STONE_ITEM;
		DamageSource damageSource = event.getSource();
		Player player = EntityHelper.getPlayerFromEntity( damageSource.getDirectEntity() );
		LivingEntity target = event.getEntityLiving();
		if( player == null || player.level.isClientSide || !magmaStone.hasAny( player ) )
			return;

		double fireDuration = magmaStone.getDuration( player );
		target.setSecondsOnFire( TimeConverter.secondsToTicks( fireDuration ) );
		ParticleHelper.spawnSmeltParticles( ( ServerLevel )player.level, target.position(), 10 );
	}

	@SubscribeEvent
	public static void onGeneratingLoot( AnyLootModificationEvent event ) {
		if( !( event.killer instanceof Player ) || !( event.entity instanceof MagmaCube ) || !( event.entity.level instanceof ServerLevel ) )
			return;

		MagmaStoneItem magmaStone = Instances.MAGMA_STONE_ITEM;
		if( Random.tryChance( magmaStone.getDropChance() ) ) {
			event.generatedLoot.add( magmaStone.getRandomInstance() );
			ParticleHelper.spawnAwardParticles( ( ServerLevel )event.entity.level, event.entity.position(), 10, 0.25 );
		}
	}

	/** Returns final duration of fire. */
	protected double getDuration( Player player ) {
		return ( int )Math.round( this.fireDuration.getValue() * ( 1.0 + getHighestEffectiveness( player ) ) );
	}

	/** Returns a chance for Magma Stone to drop from Magma Cube. */
	public double getDropChance() {
		return this.dropChance.get();
	}
}
