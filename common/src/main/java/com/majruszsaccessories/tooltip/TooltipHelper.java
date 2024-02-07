package com.majruszsaccessories.tooltip;

import com.majruszlibrary.math.Range;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TooltipHelper {
	public final static ChatFormatting DEFAULT_FORMAT = ChatFormatting.GRAY;

	public static MutableComponent asFormula( Object base, Object bonus ) {
		return TextHelper.translatable( "majruszsaccessories.items.formula", base, bonus ).withStyle( DEFAULT_FORMAT );
	}

	public static MutableComponent asRange( Object base, Object bonus ) {
		return TextHelper.translatable( "majruszsaccessories.items.range", base, bonus ).withStyle( DEFAULT_FORMAT );
	}

	public static IntegerTooltip asValue( RangedInteger value ) {
		return new IntegerTooltip( value );
	}

	public static FloatTooltip asValue( RangedFloat value ) {
		return new FloatTooltip( value );
	}

	public static IntegerTooltip asFixedValue( RangedInteger value ) {
		return new IntegerTooltip( value ).bonusMultiplier( 0 );
	}

	public static FloatTooltip asFixedValue( RangedFloat value ) {
		return new FloatTooltip( value ).bonusMultiplier( 0 );
	}

	public static PercentTooltip asPercent( RangedFloat value ) {
		return new PercentTooltip( value );
	}

	public static PercentTooltip asFixedPercent( RangedFloat value ) {
		return new PercentTooltip( value ).bonusMultiplier( 0.0f );
	}

	public static ITooltipProvider asBooster( Supplier< BoosterItem > item ) {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				if( !holder.isValid() ) {
					return TextHelper.literal( "" );
				}

				return TextHelper.translatable( "majruszsaccessories.items.booster_name", item.get().getDescription() )
					.withStyle( item.get().getRarity( ItemStack.EMPTY ).color )
					.append( " " );
			}
		};
	}

	public static ITooltipProvider asItem( Supplier< Item > item ) {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				return item.get().getDescription().copy();
			}
		};
	}

	public static ITooltipProvider asEntity( Supplier< EntityType< ? > > type ) {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				return type.get().getDescription().copy();
			}
		};
	}

	public static class IntegerTooltip implements ITooltipProvider {
		private final RangedInteger value;
		private int bonusMultiplier = 1;
		private int valueMultiplier = 1;

		IntegerTooltip( RangedInteger value ) {
			this.value = value;
		}

		@Override
		public MutableComponent getTooltip( AccessoryHolder holder ) {
			int bonusValue = holder.apply( this.value, this.bonusMultiplier ) * this.valueMultiplier;
			int defaultValue = this.value.get() * this.valueMultiplier;
			int diff = bonusValue - defaultValue;

			return TextHelper.literal( "%d".formatted( bonusValue ) )
				.withStyle( diff != 0 ? holder.getBonusFormatting() : DEFAULT_FORMAT );
		}

		@Override
		public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
			int bonusValue = holder.apply( this.value, this.bonusMultiplier ) * this.valueMultiplier;
			int defaultValue = this.value.get() * this.valueMultiplier;
			int diff = bonusValue - defaultValue;
			MutableComponent component = diff != 0 ? TextHelper.literal( TextHelper.signed( diff ) ) : TextHelper.literal( "" );

			return TooltipHelper.asFormula( defaultValue, component.withStyle( holder.getBonusFormatting() ) );
		}

		@Override
		public MutableComponent getRangeTooltip( AccessoryHolder holder ) {
			Range< Float > range = holder.getBonusRange();
			int minValue = AccessoryHolder.apply( range.from, this.value, this.bonusMultiplier ) * this.valueMultiplier;
			int maxValue = AccessoryHolder.apply( range.to, this.value, this.bonusMultiplier ) * this.valueMultiplier;
			int defaultValue = this.value.get() * this.valueMultiplier;
			MutableComponent minComponent = TextHelper.literal( "" + minValue )
				.withStyle( minValue != defaultValue ? AccessoryHolder.getBonusFormatting( range.from ) : DEFAULT_FORMAT );

			if( minValue != maxValue ) {
				MutableComponent maxComponent = TextHelper.literal( "" + maxValue )
					.withStyle( maxValue != defaultValue ? AccessoryHolder.getBonusFormatting( range.to ) : DEFAULT_FORMAT );

				return TooltipHelper.asRange( minComponent, maxComponent );
			} else {
				return minComponent;
			}
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
		private final RangedFloat value;
		private float bonusMultiplier = 1.0f;
		private float valueMultiplier = 1.0f;
		private float diffMargin = 0.001f;
		private int scale = 2;

		FloatTooltip( RangedFloat value ) {
			this.value = value;
		}

		@Override
		public MutableComponent getTooltip( AccessoryHolder holder ) {
			float bonusValue = holder.apply( this.value, this.bonusMultiplier ) * this.valueMultiplier;
			float defaultValue = this.value.get() * this.valueMultiplier;
			float diff = bonusValue - defaultValue;

			return TextHelper.literal( TextHelper.minPrecision( bonusValue, this.scale ) )
				.withStyle( Math.abs( diff ) >= this.diffMargin ? holder.getBonusFormatting() : DEFAULT_FORMAT );
		}

		@Override
		public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
			float bonusValue = holder.apply( this.value, this.bonusMultiplier ) * this.valueMultiplier;
			float defaultValue = this.value.get() * this.valueMultiplier;
			float diff = bonusValue - defaultValue;
			MutableComponent component = Math.abs( diff ) >= this.diffMargin ? TextHelper.literal( TextHelper.signed( diff, this.scale ) ) : TextHelper.literal( "" );

			return TooltipHelper.asFormula( TextHelper.minPrecision( defaultValue, this.scale ), component.withStyle( holder.getBonusFormatting() ) );
		}

		@Override
		public MutableComponent getRangeTooltip( AccessoryHolder holder ) {
			Range< Float > range = holder.getBonusRange();
			float minValue = AccessoryHolder.apply( range.from, this.value, this.bonusMultiplier ) * this.valueMultiplier;
			float maxValue = AccessoryHolder.apply( range.to, this.value, this.bonusMultiplier ) * this.valueMultiplier;
			MutableComponent minComponent = TextHelper.literal( TextHelper.minPrecision( minValue, this.scale ) )
				.withStyle( AccessoryHolder.getBonusFormatting( range.from ) );

			if( Math.abs( maxValue - minValue ) >= this.diffMargin ) {
				return TooltipHelper.asRange(
					minComponent,
					TextHelper.literal( TextHelper.minPrecision( maxValue, this.scale ) ).withStyle( AccessoryHolder.getBonusFormatting( range.to ) )
				);
			} else {
				return minComponent;
			}
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
			this.diffMargin = ( float )Math.pow( 0.1, scale + 2 );

			return this;
		}
	}

	public static class PercentTooltip implements ITooltipProvider {
		private final RangedFloat value;
		private float bonusMultiplier = 1.0f;
		private float valueMultiplier = 1.0f;
		private float diffMargin = 0.001f;
		private int scale = 2;

		PercentTooltip( RangedFloat value ) {
			this.value = value;
		}

		@Override
		public MutableComponent getTooltip( AccessoryHolder holder ) {
			float bonusValue = holder.apply( this.value, this.bonusMultiplier ) * this.valueMultiplier;
			float defaultValue = this.value.get() * this.valueMultiplier;
			float diff = bonusValue - defaultValue;

			return TextHelper.literal( TextHelper.percent( bonusValue, this.scale ) )
				.withStyle( Math.abs( diff ) >= this.diffMargin ? holder.getBonusFormatting() : DEFAULT_FORMAT );
		}

		@Override
		public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
			float bonusValue = holder.apply( this.value, this.bonusMultiplier ) * this.valueMultiplier;
			float defaultValue = this.value.get() * this.valueMultiplier;
			float diff = bonusValue - defaultValue;
			MutableComponent component = Math.abs( diff ) >= this.diffMargin ? TextHelper.literal( TextHelper.signedPercent( diff, this.scale ) ) : TextHelper.literal( "" );

			return TooltipHelper.asFormula( TextHelper.percent( defaultValue, this.scale ), component.withStyle( holder.getBonusFormatting() ) );
		}

		@Override
		public MutableComponent getRangeTooltip( AccessoryHolder holder ) {
			Range< Float > range = holder.getBonusRange();
			float minValue = AccessoryHolder.apply( range.from, this.value, this.bonusMultiplier ) * this.valueMultiplier;
			float maxValue = AccessoryHolder.apply( range.to, this.value, this.bonusMultiplier ) * this.valueMultiplier;
			MutableComponent minComponent = TextHelper.literal( TextHelper.percent( minValue, this.scale ) )
				.withStyle( AccessoryHolder.getBonusFormatting( range.from ) );

			if( Math.abs( maxValue - minValue ) >= this.diffMargin ) {
				return TooltipHelper.asRange(
					minComponent,
					TextHelper.literal( TextHelper.percent( maxValue, this.scale ) ).withStyle( AccessoryHolder.getBonusFormatting( range.to ) )
				);
			} else {
				return minComponent;
			}
		}

		public PercentTooltip bonusMultiplier( float multiplier ) {
			this.bonusMultiplier = multiplier;

			return this;
		}

		public PercentTooltip valueMultiplier( float multiplier ) {
			this.valueMultiplier = multiplier;

			return this;
		}

		public PercentTooltip scale( int scale ) {
			this.scale = scale;
			this.diffMargin = ( float )Math.pow( 0.1, scale + 2 );

			return this;
		}
	}
}
