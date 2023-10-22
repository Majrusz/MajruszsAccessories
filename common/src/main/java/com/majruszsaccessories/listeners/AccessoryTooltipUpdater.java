package com.majruszsaccessories.listeners;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.annotation.AutoInstance;
import com.mlib.client.ClientHelper;
import com.mlib.contexts.OnItemTooltip;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Contexts;
import com.mlib.math.Range;
import com.mlib.platform.Integration;
import com.mlib.text.TextHelper;
import com.mlib.time.TimeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@AutoInstance
public class AccessoryTooltipUpdater {
	static final int PAGE_SIZE = 7;

	public AccessoryTooltipUpdater() {
		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.itemStack.getItem() instanceof AccessoryItem ) );
	}

	private void addTooltip( OnItemTooltip data ) {
		AccessoryHolder holder = AccessoryHolder.create( data.itemStack );
		List< Component > components = new ArrayList<>();
		if( holder.hasBonusRangeDefined() && !holder.hasBonusDefined() ) {
			components.addAll( this.buildBonusRangeInfo( holder ) );
		} else {
			components.addAll( this.buildBonusInfo( holder ) );
		}
		components.addAll( this.buildUseInfo( holder, data.player ) );
		components.addAll( this.buildEffectsInfo( holder ) );

		data.components.addAll( 1, components );
	}

	private List< Component > buildBonusRangeInfo( AccessoryHolder holder ) {
		Range< Float > range = holder.getBonusRange();
		Component min = TextHelper.literal( TextHelper.signedPercent( range.from ) ).withStyle( AccessoryHolder.getBonusFormatting( range.from ) );
		Component max = TextHelper.literal( TextHelper.signedPercent( range.to ) ).withStyle( AccessoryHolder.getBonusFormatting( range.to ) );

		return List.of( TextHelper.translatable( Tooltips.BONUS, TooltipHelper.asRange( min, max ) ).withStyle( ChatFormatting.GRAY ) );
	}

	private List< Component > buildBonusInfo( AccessoryHolder holder ) {
		float baseBonus = holder.getBaseBonus();
		float bonus = holder.getBonus();
		if( baseBonus == 0.0f && bonus == 0.0f ) {
			return List.of();
		}

		MutableComponent component;
		if( ClientHelper.isShiftDown() && holder.getExtraBonus() != 0.0f ) {
			component = TooltipHelper.asFormula(
				TextHelper.literal( TextHelper.signedPercent( baseBonus ) ).withStyle( AccessoryHolder.getBonusFormatting( baseBonus ) ),
				TextHelper.literal( TextHelper.signedPercent( holder.getExtraBonus() ) ).withStyle( AccessoryHolder.getBonusFormatting( bonus ) )
			);
		} else {
			component = TextHelper.literal( TextHelper.signedPercent( bonus ) );
		}
		return List.of( TextHelper.translatable( Tooltips.BONUS, component ).withStyle( holder.getBonusFormatting() ) );
	}

	private List< Component > buildUseInfo( AccessoryHolder holder, @Nullable Player player ) {
		if( Integration.isLoaded( "curios" ) ) {
			return List.of();
		}

		return List.of( TextHelper.translatable( Tooltips.INVENTORY ).withStyle( this.getUseFormatting( holder, player ) ) );
	}

	private ChatFormatting getUseFormatting( AccessoryHolder holder, @Nullable Player player ) {
		if( player != null && AccessoryHolder.find( player, holder.getItem() ).getItemStack() == holder.getItemStack() ) {
			return ChatFormatting.GOLD;
		} else {
			return ChatFormatting.DARK_GRAY;
		}
	}

	private List< Component > buildEffectsInfo( AccessoryHolder holder ) {
		OnAccessoryTooltip tooltipData = Contexts.dispatch( new OnAccessoryTooltip( holder ) );
		boolean cannotFitSinglePage = tooltipData.components.size() > PAGE_SIZE;
		if( cannotFitSinglePage ) {
			return this.getCurrentPageSublist( tooltipData.components );
		} else {
			return tooltipData.components;
		}
	}

	private List< Component > getCurrentPageSublist( List< Component > components ) {
		int totalPages = ( int )Math.ceil( ( double )components.size() / PAGE_SIZE );
		int currentPage = ( int )( Math.floor( ( double )TimeHelper.getTicks() / TimeHelper.toTicks( PAGE_SIZE * 2.0 ) ) % totalPages );
		List< Component > pageComponents = new ArrayList<>( components.subList( currentPage * PAGE_SIZE, Math.min( ( currentPage + 1 ) * PAGE_SIZE, components.size() ) ) );
		pageComponents.add( TextHelper.translatable( Tooltips.PAGE, currentPage + 1, totalPages ).withStyle( ChatFormatting.DARK_GRAY ) );

		return pageComponents;
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
		static final String PAGE = "majruszsaccessories.items.page";
	}
}
