package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.contexts.OnAccessoryExtraBonusGet;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.particles.BonusParticleType;
import com.mlib.contexts.base.Contexts;
import com.mlib.data.Serializables;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AccessoryHolder {
	final ItemStack itemStack;
	final AccessoryItem item;
	final Data data;

	public static AccessoryHolder find( LivingEntity entity, Predicate< ItemStack > predicate ) {
		if( MajruszsAccessories.SLOT_INTEGRATION.isInstalled() ) {
			return new AccessoryHolder( MajruszsAccessories.SLOT_INTEGRATION.find( entity, predicate ) );
		} else {
			ItemStack itemStack = entity.getOffhandItem();
			if( predicate.test( itemStack ) ) {
				return new AccessoryHolder( itemStack );
			}

			return new AccessoryHolder( ItemStack.EMPTY );
		}
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
		return ParticleEmitter.of( new BonusParticleType.Options( AccessoryHolder.getRarity( bonus ).color.getColor() ) );
	}

	private AccessoryHolder( ItemStack itemStack, Data data ) {
		this.itemStack = itemStack;
		this.item = itemStack.getItem() instanceof AccessoryItem item ? item : null;
		this.data = data;
		this.data.extraBonus = AccessoryHolder.round( Contexts.dispatch( new OnAccessoryExtraBonusGet( this ) ).bonus );
	}

	private AccessoryHolder( ItemStack itemStack ) {
		this( itemStack, itemStack.getTag() != null ? Serializables.read( new Data(), itemStack.getTag() ) : new Data() );
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
			this.data.range = null;
		} );
	}

	public AccessoryHolder setBonus( Range< Float > bonus ) {
		if( ( bonus.to - bonus.from ) > 1e-5f ) {
			return this.save( ()->this.data.range = Range.of( AccessoryHolder.round( bonus.from ), AccessoryHolder.round( bonus.to ) ) );
		} else {
			return this.setBonus( bonus.from );
		}
	}

	public AccessoryHolder addBooster( BoosterItem item ) {
		return this.save( ()->this.data.boosters.add( new BoosterDef( item ) ) );
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

	public List< BoosterItem > getBoosters() {
		return this.data.boosters.stream().map( booster->booster.item ).toList();
	}

	public int getBoosterSlotsLeft() {
		return Math.max( this.item.getBoosterSlotsCount() - this.data.boosters.size(), 0 );
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
		return this.data.range != null;
	}

	public boolean hasMaxBonus() {
		return this.getBaseBonus() == MajruszsAccessories.CONFIG.efficiency.range.to;
	}

	public boolean hasBooster( BoosterItem item ) {
		return this.data.boosters.stream().anyMatch( booster->booster.item == item );
	}

	public boolean hasBooster() {
		return !this.data.boosters.isEmpty();
	}

	private AccessoryHolder save( Runnable runnable ) {
		runnable.run();
		Serializables.write( this.data, this.itemStack.getOrCreateTag() );

		return this;
	}

	private static float round( float value ) {
		return Math.round( 100.0f * value ) / 100.0f;
	}

	private static class Data {
		static {
			Serializables.get( Data.class )
				.define( "Bonus", subconfig->{
					subconfig.defineFloat( "Value", s->s.baseBonus, ( s, v )->s.baseBonus = v );
					subconfig.defineFloatRange( "ValueRange", s->s.range, ( s, v )->s.range = v );
					subconfig.defineCustomList( "Boosters", s->s.boosters, ( s, v )->s.boosters = v, BoosterDef::new );
				} );
		}

		Float baseBonus = null;
		Float extraBonus = 0.0f;
		Range< Float > range = null;
		List< BoosterDef > boosters = List.of();

		public Data( Data data ) {
			this.baseBonus = data.baseBonus;
			this.range = data.range != null ? Range.of( data.range.from, data.range.to ) : null;
			this.boosters = new ArrayList<>( data.boosters );
		}

		public Data() {}
	}

	private static class BoosterDef {
		static {
			Serializables.get( BoosterDef.class )
				.defineLocation( "Id", s->s.id, ( s, v )->{
					s.item = Registries.getItem( v ) instanceof BoosterItem booster ? booster : null;
					s.id = v;
				} );
		}

		BoosterItem item = null;
		ResourceLocation id = null;

		public BoosterDef( BoosterItem item ) {
			this.item = item;
			this.id = Registries.get( item );
		}

		public BoosterDef() {}
	}
}
