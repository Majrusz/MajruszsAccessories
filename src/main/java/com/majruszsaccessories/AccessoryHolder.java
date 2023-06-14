package com.majruszsaccessories;

import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.boosters.BoosterItem;
import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.math.Range;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

public class AccessoryHolder {
	public static final Range< Float > BONUS_RANGE = new Range<>( -0.6f, 0.6f );
	final ItemStack itemStack;
	final AccessoryItem item;

	public static AccessoryHolder find( LivingEntity entity, Predicate< ItemStack > predicate ) {
		if( Integration.isCuriosInstalled() ) {
			/* TODO: Optional< SlotResult > slotResult = CuriosApi.getCuriosHelper().findFirstCurio( entity, predicate );
			if( slotResult.isPresent() ) {
				return new AccessoryHolder( slotResult.get().stack() );
			}*/
		} else {
			ItemStack itemStack = entity.getOffhandItem();
			if( predicate.test( itemStack ) ) {
				return new AccessoryHolder( itemStack );
			}
		}

		return new AccessoryHolder( ItemStack.EMPTY );
	}

	public static AccessoryHolder find( LivingEntity entity, AccessoryItem item ) {
		return find( entity, itemStack->itemStack.is( item ) );
	}

	public static AccessoryHolder find( LivingEntity entity, BoosterItem item ) {
		return find( entity, itemStack->AccessoryHolder.create( itemStack ).hasBoosterTag( item ) );
	}

	public static boolean hasAccessory( LivingEntity entity, AccessoryItem item ) {
		return find( entity, item ).isValid();
	}

	public static boolean hasBooster( LivingEntity entity, BoosterItem item ) {
		return find( entity, item ).isValid();
	}

	public static AccessoryHolder create( AccessoryItem item ) {
		return new AccessoryHolder( new ItemStack( item ) );
	}

	public static AccessoryHolder create( ItemStack itemStack ) {
		return new AccessoryHolder( itemStack );
	}

	public static ChatFormatting getBonusFormatting( float bonus ) {
		if( bonus == AccessoryHolder.BONUS_RANGE.to ) {
			return ChatFormatting.GOLD;
		} else if( bonus >= 0.0f ) {
			return ChatFormatting.GREEN;
		} else {
			return ChatFormatting.RED;
		}
	}

	public static Rarity getItemRarity( float bonus ) {
		if( bonus == AccessoryHolder.BONUS_RANGE.to ) {
			return Rarity.EPIC;
		} else if( bonus >= 0.0f ) {
			return Rarity.RARE;
		} else {
			return Rarity.UNCOMMON;
		}
	}

	public static float randomBonus() {
		float gaussianRandom = ( float )Mth.clamp( Random.nextGaussian() / 4.0f, -1.0f, 1.0f ); // random value from range [-1.0; 1.0] with mean ~= 0.0 and standard deviation ~= 0.25
		float ratio = ( gaussianRandom + 1.0f ) / 2.0f; // random value from range [0.0; 1.0] with mean ~= 0.5 and standard deviation ~= 0.125

		return Mth.lerp( ratio, BONUS_RANGE.from, BONUS_RANGE.to );
	}

	private AccessoryHolder( ItemStack itemStack ) {
		this.itemStack = itemStack;
		this.item = itemStack.getItem() instanceof AccessoryItem item ? item : null;
	}

	public AccessoryHolder copy() {
		AccessoryHolder copy = new AccessoryHolder( new ItemStack( this.item ) );
		copy.setBonus( this.getBonus() );

		return copy;
	}

	public int apply( IntegerConfig config, int multiplier ) {
		return config.getRange().clamp( Math.round( ( 1.0f + multiplier * this.getBonus() ) * config.get() ) );
	}

	public int apply( IntegerConfig config ) {
		return this.apply( config, 1 );
	}

	public float apply( DoubleConfig config, double multiplier ) {
		return config.getRange().clamp( ( 1.0f + multiplier * this.getBonus() ) * config.asFloat() ).floatValue();
	}

	public float apply( DoubleConfig config ) {
		return this.apply( config, 1.0 );
	}

	public AccessoryHolder setRandomBonus() {
		if( this.hasBonusRangeTag() ) {
			float minBonus = this.getFloatTag( Tags.VALUE_MIN );
			float maxBonus = this.getFloatTag( Tags.VALUE_MAX );

			return this.setTagValue( Tags.VALUE, Mth.lerp( Random.nextFloat( 0.0f, 1.0f ), minBonus, maxBonus ) );
		} else {
			return this.setTagValue( Tags.VALUE, randomBonus() );
		}
	}

	public AccessoryHolder setBonus( float bonus ) {
		return this.setTagValue( Tags.VALUE, bonus );
	}

	public AccessoryHolder setBonus( Range< Float > bonus ) {
		if( ( bonus.to - bonus.from ) > 1e-5f ) {
			return this.setTagValue( Tags.VALUE_MIN, bonus.from ).setTagValue( Tags.VALUE_MAX, bonus.to );
		} else {
			return this.setBonus( bonus.from );
		}
	}

	public AccessoryHolder setBooster( BoosterItem item ) {
		return this.setTagValue( Tags.BOOSTER, Utility.getRegistryString( item ) );
	}

	public float getBonus() {
		return this.getFloatTag( Tags.VALUE );
	}

	public Range< Float > getBonusRange() {
		return new Range<>( this.getFloatTag( Tags.VALUE_MIN ), this.getFloatTag( Tags.VALUE_MAX ) );
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public AccessoryItem getItem() {
		return this.item;
	}

	public ChatFormatting getBonusFormatting() {
		return getBonusFormatting( this.getBonus() );
	}

	public Rarity getItemRarity() {
		return getItemRarity( this.getBonus() );
	}

	public boolean isValid() {
		return this.item != null;
	}

	public boolean hasBonusTag() {
		CompoundTag tag = this.itemStack.getTagElement( Tags.BONUS );

		return tag != null && tag.contains( Tags.VALUE );
	}

	public boolean hasBonusRangeTag() {
		CompoundTag tag = this.itemStack.getTagElement( Tags.BONUS );

		return tag != null && tag.contains( Tags.VALUE_MIN ) && tag.contains( Tags.VALUE_MAX );
	}

	public boolean hasMaxBonus() {
		return this.getBonus() == BONUS_RANGE.to;
	}

	public boolean hasBoosterTag( BoosterItem item ) {
		return Utility.getRegistryString( item ).equals( this.getStringTag( Tags.BOOSTER ) );
	}

	public boolean hasBoosterTag() {
		return !this.getStringTag( Tags.BOOSTER ).isEmpty();
	}

	private AccessoryHolder setTagValue( String tag, float value ) {
		this.itemStack.getOrCreateTagElement( Tags.BONUS ).putFloat( tag, Math.round( 100.0f * value ) / 100.0f );

		return this;
	}

	private AccessoryHolder setTagValue( String tag, String value ) {
		this.itemStack.getOrCreateTagElement( Tags.BONUS ).putString( tag, value );

		return this;
	}

	private float getFloatTag( String tag ) {
		CompoundTag itemTag = this.itemStack.getTagElement( Tags.BONUS );

		return itemTag != null ? itemTag.getFloat( tag ) : 0.0f;
	}

	private String getStringTag( String tag ) {
		CompoundTag itemTag = this.itemStack.getTagElement( Tags.BONUS );

		return itemTag != null ? itemTag.getString( tag ) : "";
	}

	static final class Tags {
		static final String BONUS = "Bonus";
		static final String VALUE = "Value";
		static final String VALUE_MIN = "ValueMin";
		static final String VALUE_MAX = "ValueMax";
		static final String BOOSTER = "Booster";
	}
}
