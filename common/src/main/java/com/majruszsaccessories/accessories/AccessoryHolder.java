package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.contexts.OnAccessoryExtraBonusGet;
import com.mlib.contexts.base.Contexts;
import com.mlib.data.Serializable;
import com.mlib.data.SerializableHelper;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.math.Random;
import com.mlib.math.Range;
import com.mlib.registry.Registries;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.function.Predicate;

public class AccessoryHolder {
	final ItemStack itemStack;
	final AccessoryItem item;
	final Data data;

	public static AccessoryHolder find( LivingEntity entity, Predicate< ItemStack > predicate ) {
		ItemStack itemStack = entity.getOffhandItem();
		if( predicate.test( itemStack ) ) {
			return new AccessoryHolder( itemStack );
		}

		return new AccessoryHolder( ItemStack.EMPTY );
	}

	public static AccessoryHolder find( LivingEntity entity, AccessoryItem item ) {
		return AccessoryHolder.find( entity, itemStack->itemStack.is( item ) );
	}

	public static AccessoryHolder find( LivingEntity entity, BoosterItem item ) {
		return AccessoryHolder.find( entity, itemStack->AccessoryHolder.create( itemStack ).hasBooster( item ) );
	}

	public static boolean hasAccessory( LivingEntity entity, AccessoryItem item ) {
		return AccessoryHolder.find( entity, item ).isValid();
	}

	public static boolean hasBooster( LivingEntity entity, BoosterItem item ) {
		return AccessoryHolder.find( entity, item ).isValid();
	}

	public static AccessoryHolder create( Item item ) {
		return new AccessoryHolder( new ItemStack( item ) );
	}

	public static AccessoryHolder create( ItemStack itemStack ) {
		return new AccessoryHolder( itemStack );
	}

	public static int apply( float bonus, RangedInteger value, int multiplier ) {
		return value.getRange().clamp( Math.round( ( 1.0f + multiplier * bonus ) * value.get() ) );
	}

	public static float apply( float bonus, RangedFloat value, float multiplier ) {
		return value.getRange().clamp( ( 1.0f + multiplier * bonus ) * value.get() );
	}

	public static ChatFormatting getBonusFormatting( float bonus ) {
		if( bonus > MajruszsAccessories.CONFIG.efficiency.range.to ) {
			return ChatFormatting.DARK_AQUA;
		} else if( bonus == MajruszsAccessories.CONFIG.efficiency.range.to ) {
			return ChatFormatting.GOLD;
		} else if( bonus > 0.0f ) {
			return ChatFormatting.GREEN;
		} else if( bonus == 0.0f ) {
			return ChatFormatting.GRAY;
		} else {
			return ChatFormatting.RED;
		}
	}

	public static Rarity getRarity( float bonus ) {
		if( bonus >= MajruszsAccessories.CONFIG.efficiency.range.to ) {
			return Rarity.EPIC;
		} else if( bonus >= 0.0f ) {
			return Rarity.RARE;
		} else {
			return Rarity.UNCOMMON;
		}
	}

	public static ParticleEmitter getParticleEmitter( float bonus ) {
		if( bonus >= MajruszsAccessories.CONFIG.efficiency.range.to ) {
			return ParticleEmitter.of( MajruszsAccessories.BONUS_STRONG_PARTICLE );
		} else if( bonus >= 0.0f ) {
			return ParticleEmitter.of( MajruszsAccessories.BONUS_NORMAL_PARTICLE );
		} else {
			return ParticleEmitter.of( MajruszsAccessories.BONUS_WEAK_PARTICLE );
		}
	}

	private AccessoryHolder( ItemStack itemStack, Data data ) {
		this.itemStack = itemStack;
		this.item = itemStack.getItem() instanceof AccessoryItem item ? item : null;
		this.data = data;
		this.data.extraBonus = AccessoryHolder.round( Contexts.dispatch( new OnAccessoryExtraBonusGet( this ) ).bonus );
	}

	private AccessoryHolder( ItemStack itemStack ) {
		this( itemStack, itemStack.getTag() != null ? SerializableHelper.read( Data::new, itemStack.getTag() ) : new Data() );
	}

	public AccessoryHolder copy() {
		return new AccessoryHolder( new ItemStack( this.item ), new Data( this.data ) );
	}

	public int apply( RangedInteger value, int multiplier ) {
		return AccessoryHolder.apply( this.getBonus(), value, multiplier );
	}

	public int apply( RangedInteger value ) {
		return this.apply( value, 1 );
	}

	public float apply( RangedFloat value, float multiplier ) {
		return AccessoryHolder.apply( this.getBonus(), value, multiplier );
	}

	public float apply( RangedFloat value ) {
		return this.apply( value, 1.0f );
	}

	public AccessoryHolder setRandomBonus() {
		if( this.hasBonusRangeDefined() ) {
			return this.setBonus( Mth.lerp( Random.nextFloat( 0.0f, 1.0f ), this.data.range.from, this.data.range.to ) );
		} else {
			return this.setBonus( MajruszsAccessories.CONFIG.efficiency.getRandom() );
		}
	}

	public AccessoryHolder setBonus( float bonus ) {
		return this.save( ()->{
			this.data.baseBonus = AccessoryHolder.round( bonus );
			this.data.extraBonus = AccessoryHolder.round( Contexts.dispatch( new OnAccessoryExtraBonusGet( this ) ).bonus );
			this.data.range = Range.of( null, null );
		} );
	}

	public AccessoryHolder setBonus( Range< Float > bonus ) {
		if( ( bonus.to - bonus.from ) > 1e-5f ) {
			return this.save( ()->this.data.range = Range.of( AccessoryHolder.round( bonus.from ), AccessoryHolder.round( bonus.to ) ) );
		} else {
			return this.setBonus( bonus.from );
		}
	}

	public AccessoryHolder setBooster( BoosterItem item ) {
		return this.save( ()->this.data.boosterId = Registries.get( item ) );
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

	public Rarity getRarity() {
		return AccessoryHolder.getRarity( this.getBonus() );
	}

	public ParticleEmitter getParticleEmitter() {
		return AccessoryHolder.getParticleEmitter( this.getBonus() );
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
		return this.getBaseBonus() == MajruszsAccessories.CONFIG.efficiency.range.to;
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

	private static class Data extends Serializable {
		Float baseBonus = null;
		Float extraBonus = 0.0f;
		Range< Float > range = Range.of( null, null );
		BoosterItem booster = null;
		ResourceLocation boosterId = null;

		public Data() {
			this.defineCustom( "Bonus", subconfig->{
				subconfig.defineFloat( "Value", ()->this.baseBonus, x->this.baseBonus = x );
				subconfig.defineFloat( "ValueMin", ()->this.range.from, x->this.range.from = x );
				subconfig.defineFloat( "ValueMax", ()->this.range.to, x->this.range.to = x );
				subconfig.defineLocation( "Booster", ()->this.boosterId, x->{
					this.booster = ( BoosterItem )Registries.getItem( x );
					this.boosterId = x;
				} );
			} );
		}

		public Data( Data data ) {
			this();

			this.baseBonus = data.baseBonus;
			this.range = Range.of( data.range.from, data.range.to );
			this.boosterId = data.boosterId;
		}
	}
}
