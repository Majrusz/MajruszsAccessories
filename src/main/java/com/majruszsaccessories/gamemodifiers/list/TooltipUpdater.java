package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.client.ClientHelper;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnItemTooltipContext;
import com.mlib.gamemodifiers.data.OnItemTooltipData;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TooltipUpdater extends GameModifier {
	public TooltipUpdater() {
		super( Registries.Modifiers.DEFAULT_GROUP, "TooltipUpdater", "" );

		OnItemTooltipContext onTooltip = new OnItemTooltipContext( this::addTooltip );
		onTooltip.addCondition( data->data.itemStack.getItem() instanceof AccessoryItem );

		this.addContext( onTooltip );
	}

	private void addTooltip( OnItemTooltipData data ) {
		List< Component > components = new ArrayList<>();

		addBonusInfo( components, data );
		addUseInfo( components, data );
		addModifierInfo( components, data );

		data.tooltip.addAll( 1, components );
	}

	private void addBonusInfo( List< Component > components, OnItemTooltipData data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		float bonus = handler.getBonus();
		if( bonus == 0.0f ) {
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( Tooltips.BONUS, handler.getBonusFormatting() );
		component.addParameter( TextHelper.signedPercent( bonus ) ).insertInto( components );
	}

	private void addUseInfo( List< Component > components, OnItemTooltipData data ) {
		if( Integration.isCuriosInstalled() ) {
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( Tooltips.INVENTORY, getUseFormatting( data ) );
		component.insertInto( components );
	}

	private ChatFormatting getUseFormatting( OnItemTooltipData data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		@Nullable Player player = data.event.getEntity();
		if( player != null && handler.findAccessory( player ) == data.itemStack ) {
			return ChatFormatting.GOLD;
		} else {
			return ChatFormatting.DARK_GRAY;
		}
	}

	private void addModifierInfo( List< Component > components, OnItemTooltipData data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		handler.getHolder().forEach( AccessoryModifier.class, modifier->{
			BiConsumer< List< Component >, AccessoryHandler > consumer = ClientHelper.isShiftDown() ? modifier::buildDetailedTooltip : modifier::buildTooltip;
			consumer.accept( components, handler );
		} );
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
	}
}
