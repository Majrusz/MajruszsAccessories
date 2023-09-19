package com.majruszsaccessories.tooltip;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.boosters.BoosterItem;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TooltipHelper {
	public final static ChatFormatting DEFAULT_FORMAT = ChatFormatting.GRAY;

	public static MutableComponent asFormula( Object base, Object bonus ) {
		return Component.translatable( "majruszsaccessories.items.formula", base, bonus ).withStyle( DEFAULT_FORMAT );
	}

	public static MutableComponent asRange( Object base, Object bonus ) {
		return Component.translatable( "majruszsaccessories.items.range", base, bonus ).withStyle( DEFAULT_FORMAT );
	}

	public static IntegerTooltip asValue( IntegerConfig config ) {
		return new IntegerTooltip( config );
	}

	public static IntegerTooltip asFixedValue( IntegerConfig config ) {
		return new IntegerTooltip( config ).bonusMultiplier( 0 );
	}

	public static FloatTooltip asPercent( DoubleConfig config ) {
		return new FloatTooltip( config );
	}

	public static FloatTooltip asFixedPercent( DoubleConfig config ) {
		return new FloatTooltip( config ).bonusMultiplier( 0.0f );
	}

	public static ITooltipProvider asItem( Supplier< BoosterItem > item ) {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				if( !holder.isValid() ) {
					return Component.literal( "" );
				}

				return Component.translatable( "majruszsaccessories.items.booster_name", item.get().getDescription() )
					.withStyle( item.get().getRarity( ItemStack.EMPTY ).getStyleModifier() )
					.append( " " );
			}
		};
	}

	public static class IntegerTooltip implements ITooltipProvider {
		private final IntegerConfig config;
		private int bonusMultiplier = 1;
		private int valueMultiplier = 1;

		IntegerTooltip( IntegerConfig config ) {
			this.config = config;
		}

		@Override
		public MutableComponent getTooltip( AccessoryHolder holder ) {
			int bonusValue = holder.apply( this.config, this.bonusMultiplier ) * this.valueMultiplier;
			int defaultValue = this.config.get() * this.valueMultiplier;
			int diff = bonusValue - defaultValue;

			return Component.literal( "%d".formatted( bonusValue ) )
				.withStyle( diff != 0 ? holder.getBonusFormatting() : DEFAULT_FORMAT );
		}

		@Override
		public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
			int bonusValue = holder.apply( this.config, this.bonusMultiplier ) * this.valueMultiplier;
			int defaultValue = this.config.get() * this.valueMultiplier;
			int diff = bonusValue - defaultValue;
			MutableComponent component = diff != 0 ? Component.literal( TextHelper.signed( diff ) ) : Component.literal( "" );

			return TooltipHelper.asFormula( defaultValue, component.withStyle( holder.getBonusFormatting() ) );
		}

		public IntegerTooltip bonusMultiplier( int multiplier ) {
			this.bonusMultiplier = multiplier;

			return this;
		}

		public IntegerTooltip valueMultiplier( int multiplier ) {
			this.valueMultiplier = multiplier;

			return this;
		}
	}

	public static class FloatTooltip implements ITooltipProvider {
		private final DoubleConfig config;
		private float bonusMultiplier = 1.0f;
		private float valueMultiplier = 1.0f;
		private float diffMargin = 0.001f;
		private int scale = 2;

		FloatTooltip( DoubleConfig config ) {
			this.config = config;
		}

		@Override
		public MutableComponent getTooltip( AccessoryHolder holder ) {
			float bonusValue = holder.apply( this.config, this.bonusMultiplier ) * this.valueMultiplier;
			float defaultValue = this.config.asFloat() * this.valueMultiplier;
			float diff = bonusValue - defaultValue;

			return Component.literal( TextHelper.percent( bonusValue, this.scale ) )
				.withStyle( Math.abs( diff ) >= this.diffMargin ? holder.getBonusFormatting() : DEFAULT_FORMAT );
		}

		@Override
		public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
			float bonusValue = holder.apply( this.config, this.bonusMultiplier ) * this.valueMultiplier;
			float defaultValue = this.config.asFloat() * this.valueMultiplier;
			float diff = bonusValue - defaultValue;
			MutableComponent component = Math.abs( diff ) >= this.diffMargin ? Component.literal( TextHelper.signedPercent( diff, this.scale ) ) : Component.literal( "" );

			return TooltipHelper.asFormula( TextHelper.percent( defaultValue, this.scale ), component.withStyle( holder.getBonusFormatting() ) );
		}

		public FloatTooltip bonusMultiplier( float multiplier ) {
			this.bonusMultiplier = multiplier;

			return this;
		}

		public FloatTooltip valueMultiplier( float multiplier ) {
			this.valueMultiplier = multiplier;

			return this;
		}

		public FloatTooltip scale( int scale ) {
			this.scale = scale;
			this.diffMargin = ( float )Math.pow( 0.1, scale + 1 );

			return this;
		}
	}
}
