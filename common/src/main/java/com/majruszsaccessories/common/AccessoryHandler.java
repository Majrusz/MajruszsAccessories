package com.majruszsaccessories.common;

import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.events.OnItemDecorationsRendered;
import com.majruszlibrary.events.base.Priority;
import com.majruszlibrary.registry.RegistryObject;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.config.Config;
import com.majruszsaccessories.events.OnAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.ChatFormatting;

public class AccessoryHandler extends BonusHandler< AccessoryItem > {
	public AccessoryHandler( RegistryObject< AccessoryItem > item, Class< ? extends AccessoryHandler > clazz ) {
		super( item, clazz, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnAccessoryTooltip.listen( this::addEmptyBoostersTooltip )
			.priority( Priority.LOW )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->data.itemStack.is( this.getItem() ) );

		Serializables.getStatic( Config.Accessories.class )
			.define( item.getId(), clazz );
	}

	private void addEmptyBoostersTooltip( OnAccessoryTooltip data ) {
		for( int idx = 0; idx < data.holder.getBoosterSlotsLeft(); ++idx ) {
			data.components.add( TextHelper.translatable( "majruszsaccessories.items.booster_empty" ).withStyle( ChatFormatting.DARK_GRAY ) );
		}
	}
}
