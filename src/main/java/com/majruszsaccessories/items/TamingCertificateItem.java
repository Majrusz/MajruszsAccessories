package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.mlib.Random;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.DoubleConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

/** Certificate that increases attributes of tamed animals and prints detailed information about certain tamed animals on right mouse click. */
@Mod.EventBusSubscriber
public class TamingCertificateItem extends AccessoryItem {
	private static final String HEALTH_TRANSLATION_KEY = "item.majruszs_difficulty.taming_certificate.health";
	private static final String DAMAGE_TRANSLATION_KEY = "item.majruszs_difficulty.taming_certificate.damage";
	private static final String JUMP_TRANSLATION_KEY = "item.majruszs_difficulty.taming_certificate.jump_strength";
	private static final String SPEED_TRANSLATION_KEY = "item.majruszs_difficulty.taming_certificate.speed";
	protected final DoubleConfig dropChance;
	protected final IntegrationDoubleConfig healthMultiplier;
	protected final IntegrationDoubleConfig damageMultiplier;
	protected final IntegrationDoubleConfig horseBonusesMultiplier;

	public TamingCertificateItem() {
		super( "Certificate Of Taming", "taming_certificate", true );

		String dropComment = "Chance for Certificate of Taming to drop from taming animals.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.01, 0.0, 1.0 );

		String healthComment = "Health multiplier when the animal is tamed.";
		this.healthMultiplier = new IntegrationDoubleConfig( "HealthMultiplier", healthComment, 0.15, 0.2, 0.25, 0.0, 10.0 );

		String damageComment = "Damage multiplier when the animal is tamed.";
		this.damageMultiplier = new IntegrationDoubleConfig( "DamageMultiplier", damageComment, 0.15, 0.2, 0.25, 0.0, 10.0 );

		String horseComment = "Jump height and speed multiplier when the horse is tamed.";
		this.horseBonusesMultiplier = new IntegrationDoubleConfig( "HorseBonusesMultiplier", horseComment, 0.15, 0.2, 0.25, 0.0, 10.0 );

		this.group.addConfigs( this.dropChance, this.healthMultiplier, this.damageMultiplier, this.horseBonusesMultiplier );
	}

	@SubscribeEvent
	public static void onTaming( AnimalTameEvent event ) {
		TamingCertificateItem certificate = Instances.TAMING_CERTIFICATE_ITEM;
		PlayerEntity player = event.getTamer();
		AnimalEntity animal = event.getAnimal();

		if( !( player.getCommandSenderWorld() instanceof ServerWorld ) )
			return;

		ServerWorld world = ( ServerWorld )player.getCommandSenderWorld();
		if( Random.tryChance( certificate.getDropChance() ) ) {
			ItemStack itemStack = new ItemStack( certificate, 1 );
			certificate.setRandomEffectiveness( itemStack );

			world.addFreshEntity( new ItemEntity( world, animal.getX(), animal.getY(), animal.getZ(), itemStack ) );
		}

		if( certificate.applyBonuses( player, animal ) )
			certificate.spawnParticles( animal.position(), world, 0.4 );
	}

	/** Prints information about animal on right-click. */
	@SubscribeEvent
	public static void onRightClick( PlayerInteractEvent.EntityInteract event ) {
		if( !( event.getTarget() instanceof AnimalEntity ) || !( event.getPlayer()
			.getCommandSenderWorld() instanceof ServerWorld
		) )
			return;

		PlayerEntity player = event.getPlayer();
		AnimalEntity animal = ( AnimalEntity )event.getTarget();
		ItemStack itemStack = event.getItemStack();
		if( !( itemStack.getItem() instanceof TamingCertificateItem ) )
			return;

		IFormattableTextComponent message = new StringTextComponent(
			getTranslatedAnimalInformation( animal, Attributes.MAX_HEALTH, 0, false, HEALTH_TRANSLATION_KEY ) );

		if( AttributeHandler.hasAttribute( animal, Attributes.ATTACK_DAMAGE ) )
			message.append( getTranslatedAnimalInformation( animal, Attributes.ATTACK_DAMAGE, 0, true, DAMAGE_TRANSLATION_KEY ) );
		if( animal instanceof HorseEntity ) {
			message.append( getTranslatedAnimalInformation( animal, Attributes.JUMP_STRENGTH, 2, true, JUMP_TRANSLATION_KEY ) );
			message.append( getTranslatedAnimalInformation( animal, Attributes.MOVEMENT_SPEED, 2, true, SPEED_TRANSLATION_KEY ) );
		}
		player.displayClientMessage( message, true );

		event.setCancellationResult( ActionResultType.SUCCESS );
		event.setCanceled( true );
	}

	/** Returns formatted text with information about given attribute. */
	private static String getTranslatedAnimalInformation( AnimalEntity animal, Attribute attribute, int doublePrecision, boolean withComma,
		String translationKey
	) {
		String information = withComma ? ", " : "";
		IFormattableTextComponent translatedText = new TranslationTextComponent( translationKey );
		information += translatedText.getString() + ": ";
		information += String.format( "%." + doublePrecision + "f", animal.getAttributeValue( attribute ) );

		return information;
	}

	/**
	 Applies all bonuses to given animal if player has Certificate of Taming.

	 @return Returns whether player has Certificate of Taming and bonuses were applied.
	 */
	public boolean applyBonuses( PlayerEntity player, AnimalEntity animal ) {
		if( !hasAny( player ) )
			return false;

		AttributeHandlers.HEALTH.setValueAndApply( animal, getHealthMultiplier( player ) );
		if( AttributeHandler.hasAttribute( animal, Attributes.ATTACK_DAMAGE ) )
			AttributeHandlers.DAMAGE.setValueAndApply( animal, getDamageMultiplier( player ) );

		if( animal instanceof HorseEntity ) {
			AttributeHandlers.JUMP_HEIGHT.setValueAndApply( animal, getHorseBonusesMultiplier( player ) );
			AttributeHandlers.SPEED.setValueAndApply( animal, getHorseBonusesMultiplier( player ) );
		}

		animal.setHealth( animal.getMaxHealth() );

		return true;
	}

	/** Returns health multiplier depending on the strongest Certificate of Taming player has. */
	public double getHealthMultiplier( PlayerEntity player ) {
		return getMultiplier( player, this.healthMultiplier );
	}

	/** Returns damage multiplier depending on the strongest Certificate of Taming player has. */
	public double getDamageMultiplier( PlayerEntity player ) {
		return getMultiplier( player, this.damageMultiplier );
	}

	/** Returns jump height and speed multiplier depending on the strongest Certificate of Taming player has. */
	public double getHorseBonusesMultiplier( PlayerEntity player ) {
		return getMultiplier( player, this.horseBonusesMultiplier );
	}

	/** Returns a chance for Certificate of Taming to drop. */
	public double getDropChance() {
		return this.dropChance.get();
	}

	/** Returns multiplier depending on the strongest Certificate of Taming player has. */
	private double getMultiplier( PlayerEntity player, IntegrationDoubleConfig multiplier ) {
		return multiplier.getValue() * ( 1.0 + getHighestEffectiveness( player ) );
	}

	static class AttributeHandlers {
		private static final AttributeHandler HEALTH = new AttributeHandler( "ec10e191-9ab4-40e5-a757-91e57104d3ab", "CoTHealthMultiplier",
			Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE
		);
		private static final AttributeHandler DAMAGE = new AttributeHandler( "f1d3671c-9474-4ffd-a440-902d69bd3bd9", "CoTDamageMultiplier",
			Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE
		);
		private static final AttributeHandler JUMP_HEIGHT = new AttributeHandler( "8c065a4c-98de-4ce4-adda-41ae7a20abfb", "CoTJumpHeightMultiplier",
			Attributes.JUMP_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE
		);
		private static final AttributeHandler SPEED = new AttributeHandler( "ed1c5feb-1017-4dc2-8a8b-7a64388f0dea", "CoTSpeedMultiplier",
			Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE
		);
	}
}
