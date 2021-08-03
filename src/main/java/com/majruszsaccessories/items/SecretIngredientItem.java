package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.majruszsaccessories.config.IntegrationIntegerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/** Accessory that increases potion amplifier and duration when clicked on brewing stand. */
@Mod.EventBusSubscriber
public class SecretIngredientItem extends AccessoryItem {
	private static final String POTION_TAG = "MajruszsAccessoriesPotion";
	private static final String ENHANCED_TRANSLATION_KEY = "item.majruszs_accessories.secret_ingredient.enhanced";
	private static final String NOTIFICATION_TRANSLATION_KEY = "item.majruszs_accessories.secret_ingredient.notification";
	protected final IntegrationDoubleConfig durationBonus;
	protected final IntegrationIntegerConfig amplifierBonus;

	public SecretIngredientItem() {
		super( "Secret Ingredient", "secret_ingredient", true );

		String durationComment = "Duration bonus for all created potions.";
		this.durationBonus = new IntegrationDoubleConfig( "DurationBonus", durationComment, 0.1, 0.15, 0.2, 0.0, 5.0 );

		String amplifierComment = "Amplifier bonus for all created potions.";
		this.amplifierBonus = new IntegrationIntegerConfig( "AmplifierBonus", amplifierComment, 1, 1, 2, 0, 10 );

		this.group.addConfigs( this.durationBonus, this.amplifierBonus );
	}

	@SubscribeEvent
	public static void onBrewedPotion( PlayerInteractEvent.RightClickBlock event ) {
		SecretIngredientItem secretIngredientItem = Instances.SECRET_INGREDIENT_ITEM;
		Player player = event.getPlayer();
		Level world = player.getCommandSenderWorld();
		BlockHitResult rayTraceResult = event.getHitVec();
		BlockEntity tileEntity = world.getBlockEntity( rayTraceResult.getBlockPos() );
		ItemStack itemStack = event.getItemStack();

		if( secretIngredientItem.equals( itemStack.getItem() ) && tileEntity instanceof BrewingStandBlockEntity ) {
			BrewingStandBlockEntity brewingStand = ( BrewingStandBlockEntity )tileEntity;
			if( secretIngredientItem.upgradePotions( brewingStand, itemStack ) ) {
				event.setCanceled( true );
				event.setCancellationResult( InteractionResult.SUCCESS );
				player.displayClientMessage( new TranslatableComponent( NOTIFICATION_TRANSLATION_KEY ).withStyle( ChatFormatting.BOLD ), true );
			}
		}
	}

	/** Upgrades all potions in brewing stand if possible. */
	public boolean upgradePotions( BrewingStandBlockEntity brewingStand, ItemStack secretIngredientStack ) {
		boolean upgradedAny = false;
		for( int i = 0; i < 3; ++i ) {
			ItemStack potionStack = brewingStand.getItem( i );
			List< MobEffectInstance > effectInstanceList = PotionUtils.getMobEffects( potionStack );
			if( effectInstanceList.size() == 0 || hasPotionTag( potionStack ) )
				continue;

			upgradedAny = true;
			ItemStack buffedPotionStack = createBuffedPotion( potionStack, getBuffedEffects( effectInstanceList, secretIngredientStack ) );

			brewingStand.setItem( i, buffedPotionStack );
		}

		return upgradedAny;
	}

	/** Returns list of effects with increased duration and amplifier. */
	protected List< MobEffectInstance > getBuffedEffects( List< MobEffectInstance > effectInstanceList, ItemStack secretIngredientStack ) {
		List< MobEffectInstance > buffedMobEffectInstanceList = new ArrayList<>();
		for( MobEffectInstance effectInstance : effectInstanceList ) {
			int buffedDuration = ( int )( effectInstance.getDuration() * ( 1.0 + getDurationMultiplier( secretIngredientStack ) ) );
			int buffedAmplifier = effectInstance.getAmplifier() + getAmplifierBonus( secretIngredientStack );
			buffedMobEffectInstanceList.add( new MobEffectInstance( effectInstance.getEffect(), buffedDuration, buffedAmplifier ) );
		}

		return buffedMobEffectInstanceList;
	}

	/** Returns copy of potion with special tag and new effects. */
	protected ItemStack createBuffedPotion( ItemStack potionStack, List< MobEffectInstance > buffedMobEffectInstanceList ) {
		ItemStack buffedPotionStack = potionStack.copy();
		PotionUtils.setPotion( buffedPotionStack, Potions.AWKWARD );
		PotionUtils.setCustomEffects( buffedPotionStack, buffedMobEffectInstanceList );
		setBuffedName( buffedPotionStack, potionStack );
		addPotionTag( buffedPotionStack );

		return buffedPotionStack;
	}

	/** Adds special potion tag to given item stack. */
	protected void addPotionTag( ItemStack potionStack ) {
		CompoundTag nbt = potionStack.getOrCreateTag();
		nbt.putBoolean( POTION_TAG, true );

		potionStack.setTag( nbt );
	}

	/** Checks whether item stack has a special potion tag. */
	protected boolean hasPotionTag( ItemStack potionStack ) {
		CompoundTag nbt = potionStack.getOrCreateTag();

		return nbt.getBoolean( POTION_TAG );
	}

	/** Creates special name for buffed potion. */
	protected void setBuffedName( ItemStack buffedPotion, ItemStack potion ) {
		Component hoverText = potion.getHoverName();
		CompoundTag nbt = buffedPotion.getOrCreateTagElement( "display" );
		nbt.putString( "Name",
			"[{\"translate\":\"" + ENHANCED_TRANSLATION_KEY + "\",\"italic\":false}, {\"text\":\" \"}, {\"translate\":\"" + hoverText.getString() + "\",\"italic\":false}]"
		);

		buffedPotion.addTagElement( "display", nbt );
	}

	/** Returns total duration multiplier. */
	public double getDurationMultiplier( ItemStack itemStack ) {
		return this.durationBonus.getValue() * ( 1.0 + getEffectiveness( itemStack ) );
	}

	/** Returns amplifier bonus. */
	public int getAmplifierBonus( ItemStack itemStack ) {
		return ( int )Math.round( this.amplifierBonus.getValue() * ( 1.0 + getEffectiveness( itemStack ) ) );
	}
}
