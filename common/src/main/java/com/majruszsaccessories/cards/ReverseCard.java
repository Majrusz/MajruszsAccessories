package com.majruszsaccessories.cards;

import com.majruszlibrary.events.OnItemTooltip;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.CardItem;
import net.minecraft.ChatFormatting;

public class ReverseCard extends CardItem {
	@Override
	public void apply( AccessoryHolder holder ) {
		holder.setBonus( -holder.getBaseBonus() );
	}

	@Override
	public void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.translatable( "majruszsaccessories.cards.negate" ).withStyle( ChatFormatting.GRAY ) );
	}
}
