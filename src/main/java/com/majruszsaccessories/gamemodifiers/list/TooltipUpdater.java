package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.contexts.OnItemTooltipContext;
import com.mlib.gamemodifiers.data.OnItemTooltipData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TooltipUpdater extends AccessoryModifier {
	public TooltipUpdater() {
		super( AccessoryModifier.DEFAULT, "TooltipUpdater", "" );

		OnItemTooltipContext onTooltip = new OnItemTooltipContext( this::addTooltip );
		onTooltip.addCondition( data->data.itemStack.getItem() instanceof AccessoryItem );

		this.addContext( onTooltip );
	}

	private void addTooltip( OnItemTooltipData data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		addBonusInfo( data, handler );
	}

	private void addBonusInfo( OnItemTooltipData data, AccessoryHandler handler ) {
		float bonus = handler.getBonus();
		if( bonus == 0.0f ) {
			return;
		}

		MutableComponent component = Component.translatable( Tooltips.BONUS, String.format( "%s%.0f%%", bonus > 0.0f ? "+" : "", bonus * 100.0 ) );
		if( bonus == AccessoryHandler.MAX_BONUS ) {
			component.withStyle( ChatFormatting.GOLD );
		} else if( bonus > 0.0f ) {
			component.withStyle( ChatFormatting.GREEN );
		} else {
			component.withStyle( ChatFormatting.RED );
		}
		data.tooltip.add( 1, component );
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
	}
}
