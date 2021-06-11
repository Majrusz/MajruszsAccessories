package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

/** Idol that gives a chance of having twins after breeding. */
@Mod.EventBusSubscriber
public class IdolOfFertilityItem extends AccessoryItem {
	protected final DoubleConfig dropChance;
	protected final IntegrationDoubleConfig extraAnimalChance;

	public IdolOfFertilityItem() {
		super( "Idol Of Fertility", "idol_of_fertility" );

		String dropComment = "Chance for Idol of Fertility to drop from breeding.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.005, 0.0, 1.0 );

		String extraComment = "Chance of having twins after breeding.";
		this.extraAnimalChance = new IntegrationDoubleConfig( "ExtraAnimalChance", extraComment, 0.25, 0.35, 0.5, 0.0, 1.0 );

		this.group.addConfigs( this.dropChance, this.extraAnimalChance );
	}

	@SubscribeEvent
	public static void onBreed( BabyEntitySpawnEvent event ) {
		PlayerEntity player = event.getCausedByPlayer();
		AgeableEntity child = event.getChild();
		MobEntity parent1 = event.getParentA(), parent2 = event.getParentB();
		IdolOfFertilityItem idolOfFertility = Instances.IDOL_OF_FERTILITY_ITEM;
		if( child == null || !( child.getCommandSenderWorld() instanceof ServerWorld ) )
			return;

		ServerWorld world = ( ServerWorld )child.getCommandSenderWorld();
		idolOfFertility.tryToDrop( parent1, world );
		if( player == null || !idolOfFertility.hasAny( player ) )
			return;

		idolOfFertility.tryToSpawnAnotherChild( player, world, ( AnimalEntity )parent1, ( AnimalEntity )parent2 );
	}

	/** Tries to drop the Idol of Fertility. */
	public void tryToDrop( MobEntity parent, ServerWorld world ) {
		if( !Random.tryChance( getDropChance() ) )
			return;

		ItemStack itemStack = new ItemStack( this, 1 );
		setRandomEffectiveness( itemStack );

		world.addFreshEntity( new ItemEntity( world, parent.getX(), parent.getY(), parent.getZ(), itemStack ) );
	}

	/** Tries to spawn another child. */
	public void tryToSpawnAnotherChild( PlayerEntity player, ServerWorld world, @Nullable AnimalEntity parent1, @Nullable AnimalEntity parent2 ) {
		if( !Random.tryChance( getTwinsChance( player ) ) )
			return;

		if( parent1 == null || parent2 == null )
			return;

		AgeableEntity child2 = parent1.getBreedOffspring( world, parent2 );
		if( child2 == null )
			return;

		child2.setBaby( true );
		child2.absMoveTo( parent1.getX(), parent1.getY(), parent1.getZ(), 0.0f, 0.0f );
		world.addFreshEntity( child2 ); // adds child to the world
		spawnParticles( parent1.position(), world, 0.25 );
	}

	/** Returns current chance of having twins from breeding. */
	public double getTwinsChance( PlayerEntity player ) {
		return MathHelper.clamp( this.extraAnimalChance.getValue() * ( 1.0 + getHighestEffectiveness( player ) ), 0.0, 1.0 );
	}

	/** Returns a chance for Idol of Fertility to drop. */
	public double getDropChance() {
		return this.dropChance.get();
	}
}
