package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.MajruszLibrary;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class AccessoryHandler {
	public static final float MIN_BONUS = -0.6f, MAX_BONUS = 0.6f;
	final ItemStack itemStack;
	final AccessoryItem item;

	public AccessoryHandler( ItemStack itemStack ) {
		assert itemStack.getItem() instanceof AccessoryItem;

		this.itemStack = itemStack;
		this.item = ( AccessoryItem )itemStack.getItem();
	}

	@Nullable
	public static AccessoryHandler tryToCreate( LivingEntity entity, AccessoryItem item ) {
		ItemStack itemStack = findAccessory( entity, item );

		return itemStack != null ? new AccessoryHandler( itemStack ) : null;
	}

	public static void setup( ItemStack itemStack, float ratio ) {
		AccessoryHandler handler = new AccessoryHandler( itemStack );
		if( handler.hasBonusTag() ) {
			return;
		}

		handler.setBonus( ratio );
	}

	public static void setup( ItemStack itemStack ) {
		setup( itemStack, randomRatio() );
	}

	public static ItemStack construct( AccessoryItem item, float ratio ) {
		ItemStack itemStack = new ItemStack( item );
		setup( itemStack, ratio );

		return itemStack;
	}

	public static ItemStack construct( AccessoryItem item ) {
		return construct( item, randomRatio() );
	}

	public static float randomRatio() {
		// random value from range [-1.0; 1.0] with mean ~= 0.0 and standard deviation ~= 0.3333..
		float gaussianRandom = ( float )Mth.clamp( MajruszLibrary.RANDOM.nextGaussian() / 3.0f, -1.0f, 1.0f );

		return ( gaussianRandom + 1.0f ) / 2.0f; // random value from range [0.0; 1.0] with mean ~= 0.5 and standard deviation ~= 0.1666..
	}

	public static float ratioToBonus( float ratio ) {
		return Math.round( 100.0f * Mth.lerp( ratio, MIN_BONUS, MAX_BONUS ) ) / 100.0f;
	}

	public static boolean hasAccessory( LivingEntity entity, AccessoryItem item ) {
		return findAccessory( entity, item ) != null;
	}

	@Nullable
	public static ItemStack findAccessory( LivingEntity entity, AccessoryItem item ) {
		if( Integration.isCuriosInstalled() ) {
			Optional< SlotResult > slotResult = CuriosApi.getCuriosHelper().findFirstCurio( entity, item );
			if( slotResult.isPresent() ) {
				return slotResult.get().stack();
			}
		} else {
			ItemStack itemStack = entity.getOffhandItem();
			if( itemStack.is( item ) ) {
				return itemStack;
			}
		}

		return null;
	}

	public boolean hasAccessory( LivingEntity entity ) {
		return hasAccessory( entity, this.item );
	}

	@Nullable
	public ItemStack findAccessory( LivingEntity entity ) {
		return findAccessory( entity, this.item );
	}

	public boolean hasBonusTag() {
		return this.itemStack.getTagElement( Tags.BONUS ) != null;
	}

	public void setBonus( float ratio ) {
		assert 0.0 <= ratio && ratio <= 1.0;

		this.itemStack.getOrCreateTagElement( Tags.BONUS ).putFloat( Tags.VALUE, ratioToBonus( ratio ) );
	}

	public float getBonus() {
		return this.itemStack.getOrCreateTagElement( Tags.BONUS ).getFloat( Tags.VALUE );
	}

	public GameModifiersHolder< ? > getHolder() {
		return this.item.getHolder();
	}

	public ChatFormatting getBonusFormatting() {
		float bonus = getBonus();
		if( bonus == AccessoryHandler.MAX_BONUS ) {
			return ChatFormatting.GOLD;
		} else if( bonus > 0.0f ) {
			return ChatFormatting.GREEN;
		} else {
			return ChatFormatting.RED;
		}
	}

	static final class Tags {
		static final String BONUS = "Bonus";
		static final String VALUE = "Value";
	}
}
