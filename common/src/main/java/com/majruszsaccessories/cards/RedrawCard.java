package com.majruszsaccessories.cards;

import com.majruszlibrary.events.OnItemTooltip;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.config.Config;
import com.majruszsaccessories.items.CardItem;
import net.minecraft.ChatFormatting;

public class RedrawCard extends CardItem {
	@Override
	public void apply( AccessoryHolder holder ) {
		holder.setBonus( Config.Efficiency.RANGE, AccessoryHolder.RandomType.NORMAL_DISTRIBUTION );
	}

	@Override
	public void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.translatable( "majruszsaccessories.cards.redraw" ).withStyle( ChatFormatting.GRAY ) );
	}
}
