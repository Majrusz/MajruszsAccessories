package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Utility;
import com.mlib.annotations.AutoInstance;
import com.mlib.client.ClientHelper;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnItemTooltip;
import com.mlib.text.FormattedTranslatable;
import com.mlib.text.TextHelper;
import com.mlib.time.TimeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@AutoInstance
public class TooltipUpdater extends GameModifier {
	static final int PAGE_SIZE = 4;

	public TooltipUpdater() {
		super( Registries.Modifiers.DEFAULT_GROUP );

		new OnItemTooltip.Context( this::addTooltip )
			.addCondition( data->data.itemStack.getItem() instanceof AccessoryItem )
			.insertTo( this );

		this.name( "TooltipUpdater" );
	}

	private void addTooltip( OnItemTooltip.Data data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		List< Component > components = new ArrayList<>();
		if( handler.hasBonusRangeTag() ) {
			this.addBonusRangeInfo( components, data );
		} else {
			this.addBonusInfo( components, data );
			this.addUseInfo( components, data );
			this.addModifierInfo( components, data );
		}

		data.tooltip.addAll( 1, components );
	}

	private void addBonusRangeInfo( List< Component > components, OnItemTooltip.Data data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		AccessoryHandler.Range bonus = handler.getBonusRange();

		FormattedTranslatable component = new FormattedTranslatable( Tooltips.BONUS_RANGE, ChatFormatting.GRAY );
		component.addParameter( TextHelper.signedPercent( bonus.min() ), AccessoryHandler.getBonusFormatting( bonus.min() ) )
			.addParameter( TextHelper.signedPercent( bonus.max() ), AccessoryHandler.getBonusFormatting( bonus.max() ) )
			.insertInto( components );
	}

	private void addBonusInfo( List< Component > components, OnItemTooltip.Data data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		float bonus = handler.getBonus();
		if( bonus == 0.0f ) {
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( Tooltips.BONUS, handler.getBonusFormatting() );
		component.addParameter( TextHelper.signedPercent( bonus ) ).insertInto( components );
	}

	private void addUseInfo( List< Component > components, OnItemTooltip.Data data ) {
		if( Integration.isCuriosInstalled() ) {
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( Tooltips.INVENTORY, getUseFormatting( data ) );
		component.insertInto( components );
	}

	private ChatFormatting getUseFormatting( OnItemTooltip.Data data ) {
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		@Nullable Player player = data.event.getEntity();
		if( player != null && handler.findAccessory( player ) == data.itemStack ) {
			return ChatFormatting.GOLD;
		} else {
			return ChatFormatting.DARK_GRAY;
		}
	}

	private void addModifierInfo( List< Component > components, OnItemTooltip.Data data ) {
		List< Component > pageComponents = new ArrayList<>();
		AccessoryHandler handler = new AccessoryHandler( data.itemStack );
		handler.getModifiers().forEach( modifier->{
			BiConsumer< List< Component >, AccessoryHandler > consumer = ClientHelper.isShiftDown() ? modifier::buildDetailedTooltip : modifier::buildTooltip;
			consumer.accept( pageComponents, handler );
		} );
		boolean cannotFitSinglePage = pageComponents.size() > PAGE_SIZE;
		if( cannotFitSinglePage ) {
			int totalPages = ( int )Math.ceil( ( double )pageComponents.size() / PAGE_SIZE );
			int currentPage = ( int )( Math.floor( ( double )TimeHelper.getClientTicks() / Utility.secondsToTicks( 7.5 ) ) % totalPages );
			components.addAll( pageComponents.subList( currentPage * PAGE_SIZE, Math.min( ( currentPage + 1 ) * PAGE_SIZE, pageComponents.size() ) ) );
			components.add( Component.translatable( Tooltips.PAGE, currentPage + 1, totalPages ).withStyle( ChatFormatting.DARK_GRAY ) );
		} else {
			components.addAll( pageComponents );
		}
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
		static final String BONUS_RANGE = "majruszsaccessories.items.bonus_range";
		static final String PAGE = "majruszsaccessories.items.page";
	}
}
