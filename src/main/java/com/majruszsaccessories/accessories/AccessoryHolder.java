package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Integration;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryExtraBonusGet;
import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.data.SerializableHelper;
import com.mlib.data.SerializableStructure;
import com.mlib.math.Range;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
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
	final Data data;

	public static AccessoryHolder find( LivingEntity entity, Predicate< ItemStack > predicate ) {
		if( entity != null ) {
			if( Integration.isCuriosInstalled() ) {
				Optional< SlotResult > slotResult = CuriosApi.getCuriosHelper().findFirstCurio( entity, predicate );
				if( slotResult.isPresent() ) {
					return new AccessoryHolder( slotResult.get().stack() );
				}
			} else {
				ItemStack itemStack = entity.getOffhandItem();
				if( predicate.test( itemStack ) ) {
					return new AccessoryHolder( itemStack );
				}
			}
		}

		return new AccessoryHolder( ItemStack.EMPTY );
	}

	public static AccessoryHolder find( LivingEntity entity, AccessoryItem item ) {
		return find( entity, itemStack->itemStack.is( item ) );
	}

	public static AccessoryHolder find( LivingEntity entity, BoosterItem item ) {
		return find( entity, itemStack->AccessoryHolder.create( itemStack ).hasBooster( item ) );
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

	public static int apply( float bonus, IntegerConfig config, int multiplier ) {
		return config.getRange().clamp( Math.round( ( 1.0f + multiplier * bonus ) * config.get() ) );
	}

	public static float apply( float bonus, DoubleConfig config, float multiplier ) {
		return config.getRange().clamp( ( double )( 1.0f + multiplier * bonus ) * config.asFloat() ).floatValue();
	}

	public static ChatFormatting getBonusFormatting( float bonus ) {
		if( bonus > AccessoryHolder.BONUS_RANGE.to ) {
			return ChatFormatting.DARK_AQUA;
		} else if( bonus == AccessoryHolder.BONUS_RANGE.to ) {
			return ChatFormatting.GOLD;
		} else if( bonus > 0.0f ) {
			return ChatFormatting.GREEN;
		} else if( bonus == 0.0f ) {
			return ChatFormatting.GRAY;
		} else {
			return ChatFormatting.RED;
		}
	}

	public static Rarity getItemRarity( float bonus ) {
		if( bonus >= AccessoryHolder.BONUS_RANGE.to ) {
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

	private AccessoryHolder( ItemStack itemStack, Data data ) {
		this.itemStack = itemStack;
		this.item = itemStack.getItem() instanceof AccessoryItem item ? item : null;
		this.data = data;
		this.data.extraBonus = AccessoryHolder.round( OnAccessoryExtraBonusGet.dispatch( this ).value );
	}

	private AccessoryHolder( ItemStack itemStack ) {
		this( itemStack, itemStack.getTag() != null ? SerializableHelper.read( Data::new, itemStack.getTag() ) : new Data() );
	}

	public AccessoryHolder copy() {
		return new AccessoryHolder( new ItemStack( this.item ), new Data( this.data ) );
	}

	public int apply( IntegerConfig config, int multiplier ) {
		return AccessoryHolder.apply( this.getBonus(), config, multiplier );
	}

	public int apply( IntegerConfig config ) {
		return this.apply( config, 1 );
	}

	public float apply( DoubleConfig config, float multiplier ) {
		return AccessoryHolder.apply( this.getBonus(), config, multiplier );
	}

	public float apply( DoubleConfig config ) {
		return this.apply( config, 1.0f );
	}

	public AccessoryHolder setRandomBonus() {
		if( this.hasBonusRangeDefined() ) {
			return this.setBonus( Mth.lerp( Random.nextFloat( 0.0f, 1.0f ), this.data.range.from, this.data.range.to ) );
		} else {
			return this.setBonus( randomBonus() );
		}
	}

	public AccessoryHolder setBonus( float bonus ) {
		return this.save( ()->{
			this.data.baseBonus = AccessoryHolder.round( bonus );
			this.data.extraBonus = AccessoryHolder.round( OnAccessoryExtraBonusGet.dispatch( this ).value );
			this.data.range = new Range<>( null, null );
		} );
	}

	public AccessoryHolder setBonus( Range< Float > bonus ) {
		if( ( bonus.to - bonus.from ) > 1e-5f ) {
			return this.save( ()->this.data.range = new Range<>( AccessoryHolder.round( bonus.from ), AccessoryHolder.round( bonus.to ) ) );
		} else {
			return this.setBonus( bonus.from );
		}
	}

	public AccessoryHolder setBooster( BoosterItem item ) {
		return this.save( ()->this.data.boosterId = Utility.getRegistryKey( item ) );
	}

	public float getBonus() {
		return AccessoryHolder.round( this.getBaseBonus() + this.getExtraBonus() );
	}

	public float getBaseBonus() {
		return this.data.baseBonus != null ? this.data.baseBonus : 0.0f;
	}

	public float getExtraBonus() {
		return this.data.extraBonus;
	}

	public Range< Float > getBonusRange() {
		return this.data.range;
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public AccessoryItem getItem() {
		return this.item;
	}

	public BoosterItem getBooster() {
		return this.data.booster;
	}

	public ChatFormatting getBonusFormatting() {
		return AccessoryHolder.getBonusFormatting( this.getBonus() );
	}

	public Rarity getItemRarity() {
		return AccessoryHolder.getItemRarity( this.getBonus() );
	}

	public boolean isValid() {
		return this.item != null;
	}

	public boolean hasBonusDefined() {
		return this.data.baseBonus != null;
	}

	public boolean hasBonusRangeDefined() {
		return this.data.range.from != null && this.data.range.to != null;
	}

	public boolean hasMaxBonus() {
		return this.getBaseBonus() == BONUS_RANGE.to;
	}

	public boolean hasBooster( BoosterItem item ) {
		return this.data.booster == item;
	}

	public boolean hasBooster() {
		return this.data.boosterId != null;
	}

	private AccessoryHolder save( Runnable runnable ) {
		runnable.run();
		this.data.write( this.itemStack.getOrCreateTag() );

		return this;
	}

	private static float round( float value ) {
		return Math.round( 100.0f * value ) / 100.0f;
	}

	private static class Data extends SerializableStructure {
		Float baseBonus = null;
		Float extraBonus = 0.0f;
		Range< Float > range = new Range<>( null, null );
		BoosterItem booster = null;
		ResourceLocation boosterId = null;

		public Data() {
			super( "Bonus" );

			this.defineFloat( "Value", ()->this.baseBonus, x->this.baseBonus = x );
			this.defineFloat( "ValueMin", ()->this.range.from, x->this.range.from = x );
			this.defineFloat( "ValueMax", ()->this.range.to, x->this.range.to = x );
			this.defineLocation( "Booster", ()->this.boosterId, x->{
				this.booster = ( BoosterItem )Utility.getItem( x );
				this.boosterId = x;
			} );
		}

		public Data( Data data ) {
			this();

			this.baseBonus = data.baseBonus;
			this.range = new Range<>( data.range.from, data.range.to );
			this.boosterId = data.boosterId;
		}
	}
}
