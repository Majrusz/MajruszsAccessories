package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationDoubleConfig;
import com.majruszsaccessories.config.IntegrationIntegerConfig;
import com.mlib.MajruszLibrary;
import com.mlib.config.DoubleConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
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
	protected final IntegrationDoubleConfig durationBonus;
	protected final IntegrationIntegerConfig amplifierBonus;
	protected final DoubleConfig dropChance;

	public SecretIngredientItem() {
		super( "Secret Ingredient", "secret_ingredient", true );

		String durationComment = "Duration bonus for all created potions.";
		this.durationBonus = new IntegrationDoubleConfig( "DurationBonus", durationComment, 0.1, 0.15, 0.2, 0.0, 5.0 );

		String amplifierComment = "Amplifier bonus for all created potions.";
		this.amplifierBonus = new IntegrationIntegerConfig( "AmplifierBonus", amplifierComment, 1, 1, 2, 0, 10 );

		String dropComment = "Chance for Secret Ingredient to drop from brewing potions.";
		this.dropChance = new DoubleConfig( "drop_chance", dropComment, false, 0.001, 0.0, 1.0 );

		this.group.addConfigs( this.durationBonus, this.amplifierBonus, this.dropChance );
	}

	@SubscribeEvent
	public static void onBrewedPotion( PlayerInteractEvent.RightClickBlock event ) {
		SecretIngredientItem secretIngredientItem = Instances.SECRET_INGREDIENT_ITEM;
		PlayerEntity player = event.getPlayer();
		World world = player.getCommandSenderWorld();
		BlockRayTraceResult rayTraceResult = event.getHitVec();
		TileEntity tileEntity = world.getBlockEntity( rayTraceResult.getBlockPos() );
		ItemStack itemStack = event.getItemStack();

		if( secretIngredientItem.equals( itemStack.getItem() ) && tileEntity instanceof BrewingStandTileEntity ) {
			BrewingStandTileEntity brewingStand = ( BrewingStandTileEntity )tileEntity;
			if( secretIngredientItem.upgradePotions( brewingStand, itemStack ) ) {
				event.setCanceled( true );
				event.setCancellationResult( ActionResultType.FAIL );
				MajruszLibrary.LOGGER.info( "CANCELLED" );
			}
		}
		/*PlayerEntity player = event.getItem( 0 ).getEntityRepresentation().position()
		MajruszLibrary.LOGGER.info( event.getStack() );
		if( !( player.getCommandSenderWorld() instanceof ServerWorld ) )
			return;
		PotionItem

		ServerWorld world = ( ServerWorld )player.getCommandSenderWorld();
		if( Random.tryChance( strangePotionItem.dropChance.get() ) )
			ItemHelper.giveItemStackToPlayer( strangePotionItem.getRandomInstance(), player, world );

		if( strangePotionItem.hasAny( player ) ) {
			ItemStack potion = event.getStack();
			List< EffectInstance > effectInstanceList = PotionUtils.getMobEffects( potion ), buffedEffectInstanceList = new ArrayList<>();
			if( effectInstanceList.size() == 0 )
				return;

			for( EffectInstance effectInstance : effectInstanceList ) {
				int buffedDuration = ( int )( effectInstance.getDuration() * strangePotionItem.getDurationMultiplier( player ) );
				int buffedAmplifier = effectInstance.getAmplifier() + strangePotionItem.getAmplifierBonus( player );
				buffedEffectInstanceList.add( new EffectInstance( effectInstance.getEffect(), buffedDuration, buffedAmplifier ) );
			}

			ItemStack buffedPotion = potion.copy();
			PotionUtils.setPotion( buffedPotion, Potions.EMPTY );
			PotionUtils.setCustomEffects( buffedPotion, buffedEffectInstanceList );

			MajruszLibrary.LOGGER.info( "2" + potion + buffedPotion );
			ItemHelper.giveItemStackToPlayer( buffedPotion, player, world );
		}*/
	}

	/** Upgrades all potions in brewing stand if possible. */
	public boolean upgradePotions( BrewingStandTileEntity brewingStand, ItemStack secretIngredientStack ) {
		boolean upgradedAny = false;
		for( int i = 0; i < 3; ++i ) {
			ItemStack potionStack = brewingStand.getItem( i );
			List< EffectInstance > effectInstanceList = PotionUtils.getMobEffects( potionStack );
			if( effectInstanceList.size() == 0 || hasPotionTag( potionStack ) )
				continue;

			upgradedAny = true;
			ItemStack buffedPotionStack = createBuffedPotion( potionStack, getBuffedEffects( effectInstanceList, secretIngredientStack ) );

			brewingStand.setItem( i, buffedPotionStack );
		}

		return upgradedAny;
	}

	/** Returns list of effects with increased duration and amplifier. */
	protected List< EffectInstance > getBuffedEffects( List< EffectInstance > effectInstanceList, ItemStack secretIngredientStack ) {
		List< EffectInstance > buffedEffectInstanceList = new ArrayList<>();
		for( EffectInstance effectInstance : effectInstanceList ) {
			int buffedDuration = ( int )( effectInstance.getDuration() * ( 1.0 + getDurationMultiplier( secretIngredientStack ) ) );
			int buffedAmplifier = effectInstance.getAmplifier() + getAmplifierBonus( secretIngredientStack );
			buffedEffectInstanceList.add( new EffectInstance( effectInstance.getEffect(), buffedDuration, buffedAmplifier ) );
		}

		return buffedEffectInstanceList;
	}

	/** Returns copy of potion with special tag and new effects. */
	protected ItemStack createBuffedPotion( ItemStack potionStack, List< EffectInstance > buffedEffectInstanceList ) {
		ItemStack buffedPotionStack = potionStack.copy();
		PotionUtils.setPotion( buffedPotionStack, Potions.EMPTY );
		PotionUtils.setCustomEffects( buffedPotionStack, buffedEffectInstanceList );
		setBuffedName( buffedPotionStack, potionStack );
		addPotionTag( buffedPotionStack );

		return buffedPotionStack;
	}

	/** Adds special potion tag to given item stack. */
	protected void addPotionTag( ItemStack potionStack ) {
		CompoundNBT nbt = potionStack.getOrCreateTag();
		nbt.putBoolean( POTION_TAG, true );

		potionStack.setTag( nbt );
	}

	/** Checks whether item stack has a special potion tag. */
	protected boolean hasPotionTag( ItemStack potionStack ) {
		CompoundNBT nbt = potionStack.getOrCreateTag();

		return nbt.getBoolean( POTION_TAG );
	}

	/** Creates special name for buffed potion. */
	protected void setBuffedName( ItemStack buffedPotion, ItemStack potion ) {
		ITextComponent hoverText = potion.getHoverName();
		CompoundNBT nbt = buffedPotion.getOrCreateTagElement( "display" );
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
