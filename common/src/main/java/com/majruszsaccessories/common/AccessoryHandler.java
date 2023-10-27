package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.contexts.OnTradesInitialized;
import com.mlib.contexts.base.Priority;
import com.mlib.registry.RegistryObject;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;

public class AccessoryHandler extends BonusHandler< AccessoryItem > {
	public AccessoryHandler( RegistryObject< AccessoryItem > item ) {
		super( item, MajruszsAccessories.CONFIG.accessories, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnAccessoryTooltip.listen( this::addEmptyBoostersTooltip )
			.priority( Priority.LOW )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnTradesInitialized.listen( this::addTrades );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->data.itemStack.is( this.getItem() ) );
	}

	private void addEmptyBoostersTooltip( OnAccessoryTooltip data ) {
		for( int idx = 0; idx < data.holder.getBoosterSlotsLeft(); ++idx ) {
			data.components.add( TextHelper.translatable( "majruszsaccessories.items.booster_empty" ).withStyle( ChatFormatting.DARK_GRAY ) );
		}
	}
}
