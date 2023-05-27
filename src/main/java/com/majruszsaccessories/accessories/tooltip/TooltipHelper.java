package com.majruszsaccessories.accessories.tooltip;

import com.majruszsaccessories.AccessoryHolder;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TooltipHelper {
	public final static ChatFormatting DEFAULT_FORMAT = ChatFormatting.GRAY;

	public static MutableComponent asFormula( Object obj1, Object obj2 ) {
		return Component.translatable( "majruszsaccessories.items.formula", obj1, obj2 )
			.withStyle( DEFAULT_FORMAT );
	}

	public static ITooltipProvider asValue( IntegerConfig config, int multiplier ) {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				int bonusValue = holder.apply( config, multiplier );
				int defaultValue = config.get();
				int diff = bonusValue - defaultValue;

				return Component.literal( String.format( "%d", bonusValue ) )
					.withStyle( diff != 0 ? holder.getBonusFormatting() : DEFAULT_FORMAT );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				int bonusValue = holder.apply( config, multiplier );
				int defaultValue = config.get();
				int diff = bonusValue - defaultValue;
				MutableComponent component = diff != 0 ? Component.literal( TextHelper.signed( diff ) ) : Component.literal( "" );

				return asFormula( defaultValue, component.withStyle( holder.getBonusFormatting() ) );
			}
		};
	}

	public static ITooltipProvider asValue( IntegerConfig config ) {
		return asValue( config, 1 );
	}

	public static ITooltipProvider asPercent( DoubleConfig config, double multiplier ) {
		return new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				float bonusValue = holder.apply( config, multiplier );
				float defaultValue = config.asFloat();
				float diff = bonusValue - defaultValue;

				return Component.literal( TextHelper.percent( bonusValue ) )
					.withStyle( Math.abs( diff ) >= 0.0001f ? holder.getBonusFormatting() : DEFAULT_FORMAT );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				float bonusValue = holder.apply( config, multiplier );
				float defaultValue = config.asFloat();
				float diff = bonusValue - defaultValue;
				MutableComponent component = Math.abs( diff ) >= 0.0001f ? Component.literal( TextHelper.signedPercent( diff ) ) : Component.literal( "" );

				return asFormula( TextHelper.percent( defaultValue ), component.withStyle( holder.getBonusFormatting() ) );
			}
		};
	}

	public static ITooltipProvider asPercent( DoubleConfig config ) {
		return asPercent( config, 1.0 );
	}
}
