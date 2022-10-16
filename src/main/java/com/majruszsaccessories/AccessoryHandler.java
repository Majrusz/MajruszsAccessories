package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.MajruszLibrary;
import com.mlib.Random;
import com.mlib.gamemodifiers.GameModifiersHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
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

	public static AccessoryHandler setup( ItemStack itemStack, float bonus ) {
		AccessoryHandler handler = new AccessoryHandler( itemStack );
		if( !handler.hasBonusTag() ) {
			handler.setBonus( bonus );
		}

		return handler;
	}

	public static AccessoryHandler setup( ItemStack itemStack, float minBonus, float maxBonus ) {
		AccessoryHandler handler = new AccessoryHandler( itemStack );
		if( !handler.hasBonusTag() ) {
			handler.setBonusRange( minBonus, maxBonus );
		}

		return handler;
	}

	public static float randomBonus() {
		float gaussianRandom = ( float )Mth.clamp( MajruszLibrary.RANDOM.nextGaussian() / 3.0f, -1.0f, 1.0f ); // random value from range [-1.0; 1.0] with mean ~= 0.0 and standard deviation ~= 0.3333..
		float ratio = ( gaussianRandom + 1.0f ) / 2.0f; // random value from range [0.0; 1.0] with mean ~= 0.5 and standard deviation ~= 0.1666..

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

	public static ChatFormatting getBonusFormatting( float bonus ) {
		if( bonus == AccessoryHandler.MAX_BONUS ) {
			return ChatFormatting.GOLD;
		} else if( bonus > 0.0f ) {
			return ChatFormatting.GREEN;
		} else {
			return ChatFormatting.RED;
		}
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

	public boolean hasBonusRangeTag() {
		CompoundTag bonus = this.itemStack.getTagElement( Tags.BONUS );

		return bonus != null && bonus.contains( Tags.VALUE_MIN ) && bonus.contains( Tags.VALUE_MAX );
	}

	public void setBonus( float bonus ) {
		assert MIN_BONUS <= bonus && bonus <= MAX_BONUS;

		this.itemStack.getOrCreateTagElement( Tags.BONUS ).putFloat( Tags.VALUE, bonus );
	}

	public void setBonusRange( float minBonus, float maxBonus ) {
		assert minBonus <= maxBonus;
		assert MIN_BONUS <= minBonus && minBonus <= MAX_BONUS;
		assert MIN_BONUS <= maxBonus && maxBonus <= MAX_BONUS;

		CompoundTag bonus = this.itemStack.getOrCreateTagElement( Tags.BONUS );
		bonus.putFloat( Tags.VALUE_MIN, minBonus );
		bonus.putFloat( Tags.VALUE_MAX, maxBonus );
	}

	public void applyBonusRange() {
		assert this.hasBonusRangeTag();

		CompoundTag bonus = this.itemStack.getOrCreateTagElement( Tags.BONUS );
		float minBonus = bonus.getFloat( Tags.VALUE_MIN );
		float maxBonus = bonus.getFloat( Tags.VALUE_MAX );
		bonus.putFloat( Tags.VALUE, Math.round( 100.0f * Mth.lerp( Random.nextFloat( 0.0f, 1.0f ), minBonus, maxBonus ) ) / 100.0f );
		bonus.remove( Tags.VALUE_MIN );
		bonus.remove( Tags.VALUE_MAX );
	}

	public float getBonus() {
		return this.itemStack.getOrCreateTagElement( Tags.BONUS ).getFloat( Tags.VALUE );
	}

	public Range getBonusRange() {
		assert this.hasBonusRangeTag();

		CompoundTag bonus = this.itemStack.getOrCreateTagElement( Tags.BONUS );
		return new Range( bonus.getFloat( Tags.VALUE_MIN ), bonus.getFloat( Tags.VALUE_MAX ) );
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public GameModifiersHolder< ? > getHolder() {
		return this.item.getHolder();
	}

	public ChatFormatting getBonusFormatting() {
		return getBonusFormatting( this.getBonus() );
	}

	public record Range( float min, float max ) {}

	static final class Tags {
		static final String BONUS = "Bonus";
		static final String VALUE = "Value";
		static final String VALUE_MIN = "ValueMin", VALUE_MAX = "ValueMax";
	}
}
