package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.Random;
import com.mlib.config.DoubleConfig;
import com.mlib.particles.ParticleHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
		Player player = event.getCausedByPlayer();
		AgeableMob child = event.getChild();
		Mob parent1 = event.getParentA(), parent2 = event.getParentB();
		IdolOfFertilityItem idolOfFertility = Instances.IDOL_OF_FERTILITY_ITEM;
		if( child == null || !( child.getCommandSenderWorld() instanceof ServerLevel ) )
			return;

		ServerLevel world = ( ServerLevel )child.getCommandSenderWorld();
		idolOfFertility.tryToDrop( parent1, world );
		if( player == null || !idolOfFertility.hasAny( player ) )
			return;

		idolOfFertility.tryToSpawnAnotherChild( player, world, ( Animal )parent1, ( Animal )parent2 );
	}

	/** Tries to drop the Idol of Fertility. */
	public void tryToDrop( Mob parent, ServerLevel world ) {
		if( !Random.tryChance( this.dropChance.get() ) )
			return;

		ItemStack itemStack = new ItemStack( this, 1 );
		setRandomEffectiveness( itemStack );

		world.addFreshEntity( new ItemEntity( world, parent.getX(), parent.getY(), parent.getZ(), itemStack ) );
	}

	/** Tries to spawn another child. */
	public void tryToSpawnAnotherChild( Player player, ServerLevel level, @Nullable Animal parent1, @Nullable Animal parent2 ) {
		if( !Random.tryChance( getTwinsChance( player ) ) )
			return;

		if( parent1 == null || parent2 == null )
			return;

		AgeableMob child2 = parent1.getBreedOffspring( level, parent2 );
		if( child2 == null )
			return;

		child2.setBaby( true );
		child2.absMoveTo( parent1.getX(), parent1.getY(), parent1.getZ(), 0.0f, 0.0f );
		level.addFreshEntity( child2 ); // adds child to the world
		ParticleHelper.spawnAwardParticles( level, parent1.position(), 5, 0.25 );
	}

	/** Returns current chance of having twins from breeding. */
	public double getTwinsChance( Player player ) {
		return Mth.clamp( this.extraAnimalChance.getValue() * ( 1.0 + getHighestEffectiveness( player ) ), 0.0, 1.0 );
	}
}
