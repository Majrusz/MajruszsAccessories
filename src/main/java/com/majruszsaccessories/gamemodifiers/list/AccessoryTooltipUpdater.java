package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.Integration;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.mlib.Utility;
import com.mlib.modhelper.AutoInstance;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.OnItemTooltip;
import com.mlib.math.Range;
import com.mlib.text.TextHelper;
import com.mlib.time.TimeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@AutoInstance
public class AccessoryTooltipUpdater {
	static final int PAGE_SIZE = 7;

	public AccessoryTooltipUpdater() {
		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.itemStack.getItem() instanceof AccessoryItem ) );
	}

	private void addTooltip( OnItemTooltip.Data data ) {
		AccessoryHolder holder = AccessoryHolder.create( data.itemStack );
		List< Component > components = new ArrayList<>();
		if( holder.hasBonusRangeTag() && !holder.hasBonusTag() ) {
			components.addAll( this.buildBonusRangeInfo( data ) );
		} else {
			components.addAll( this.buildBonusInfo( data ) );
			components.addAll( this.buildUseInfo( data ) );
			components.addAll( this.buildEffectsInfo( data ) );
		}

		data.tooltip.addAll( 1, components );
	}

	private List< Component > buildBonusRangeInfo( OnItemTooltip.Data data ) {
		Range< Float > range = AccessoryHolder.create( data.itemStack ).getBonusRange();
		Component min = Component.literal( TextHelper.signedPercent( range.from ) ).withStyle( AccessoryHolder.getBonusFormatting( range.from ) );
		Component max = Component.literal( TextHelper.signedPercent( range.to ) ).withStyle( AccessoryHolder.getBonusFormatting( range.to ) );

		return List.of( Component.translatable( Tooltips.BONUS_RANGE, min, max ).withStyle( ChatFormatting.GRAY ) );
	}

	private List< Component > buildBonusInfo( OnItemTooltip.Data data ) {
		AccessoryHolder handler = AccessoryHolder.create( data.itemStack );
		float bonus = handler.getBonus();
		if( bonus == 0.0f ) {
			return List.of();
		}

		return List.of( Component.translatable( Tooltips.BONUS, TextHelper.signedPercent( bonus ) ).withStyle( handler.getBonusFormatting() ) );
	}

	private List< Component > buildUseInfo( OnItemTooltip.Data data ) {
		if( Integration.isCuriosInstalled() ) {
			return List.of();
		}

		return List.of( Component.translatable( Tooltips.INVENTORY ).withStyle( this.getUseFormatting( data ) ) );
	}

	private ChatFormatting getUseFormatting( OnItemTooltip.Data data ) {
		AccessoryHolder handler = AccessoryHolder.create( data.itemStack );
		@Nullable Player player = data.event.getEntity();
		if( player != null && AccessoryHolder.find( player, handler.getItem() ).getItemStack() == data.itemStack ) {
			return ChatFormatting.GOLD;
		} else {
			return ChatFormatting.DARK_GRAY;
		}
	}

	private List< Component > buildEffectsInfo( OnItemTooltip.Data data ) {
		OnAccessoryTooltip.Data tooltipData = OnAccessoryTooltip.dispatch( data.itemStack );
		boolean cannotFitSinglePage = tooltipData.components.size() > PAGE_SIZE;
		if( cannotFitSinglePage ) {
			return this.convertToEffectsInfoPage( tooltipData.components );
		} else {
			return tooltipData.components;
		}
	}

	private List< Component > convertToEffectsInfoPage( List< Component > components ) {
		int totalPages = ( int )Math.ceil( ( double )components.size() / PAGE_SIZE );
		int currentPage = ( int )( Math.floor( ( double )TimeHelper.getClientTicks() / Utility.secondsToTicks( PAGE_SIZE * 2 ) ) % totalPages );
		List< Component > pageComponents = new ArrayList<>( components.subList( currentPage * PAGE_SIZE, Math.min( ( currentPage + 1 ) * PAGE_SIZE, components.size() ) ) );
		pageComponents.add( Component.translatable( Tooltips.PAGE, currentPage + 1, totalPages ).withStyle( ChatFormatting.DARK_GRAY ) );

		return pageComponents;
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
		static final String BONUS_RANGE = "majruszsaccessories.items.bonus_range";
		static final String PAGE = "majruszsaccessories.items.page";
	}
}
