package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.gamemodifiers.contexts.OnItemTooltipContext;
import com.mlib.gamemodifiers.data.OnItemTooltipData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TooltipUpdater extends AccessoryModifier {
	public TooltipUpdater() {
		super( AccessoryModifier.DEFAULT, "TooltipUpdater", "" );

		OnItemTooltipContext onTooltip = new OnItemTooltipContext( this::addTooltip );
		onTooltip.addCondition( data->data.itemStack.getItem() instanceof AccessoryItem );

		this.addContext( onTooltip );
	}

	private void addTooltip( OnItemTooltipData data ) {
		List< MutableComponent > components = new ArrayList<>();

		addBonusInfo( components, data );
		addUseInfo( components, data );

		data.tooltip.addAll( 1, components );
	}

	private void addBonusInfo( List< MutableComponent > components, OnItemTooltipData data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		float bonus = handler.getBonus();
		if( bonus == 0.0f ) {
			return;
		}

		components.add( Component.translatable( Tooltips.BONUS, String.format( "%s%.0f%%", bonus > 0.0f ? "+" : "", bonus * 100.0 ) )
			.withStyle( getBonusFormatting( bonus ) ) );
	}

	private ChatFormatting getBonusFormatting( float bonus ) {
		if( bonus == AccessoryHandler.MAX_BONUS ) {
			return ChatFormatting.GOLD;
		} else if( bonus > 0.0f ) {
			return ChatFormatting.GREEN;
		} else {
			return ChatFormatting.RED;
		}
	}

	private void addUseInfo( List< MutableComponent > components, OnItemTooltipData data ) {
		if( Integration.isCuriosInstalled() ) {
			return;
		}

		components.add( Component.translatable( Tooltips.INVENTORY ).withStyle( getUseFormatting( data ) ) );
	}

	private ChatFormatting getUseFormatting( OnItemTooltipData data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		@Nullable Player player = data.event.getEntity();
		if( player != null && handler.getAccessory( player ) == data.itemStack ) {
			return ChatFormatting.GOLD;
		} else {
			return ChatFormatting.DARK_GRAY;
		}
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
	}
}
