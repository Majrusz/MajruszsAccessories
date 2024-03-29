package com.majruszsaccessories.cards;

import com.majruszlibrary.events.OnItemTooltip;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.CardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RemovalCard extends CardItem {
	@Override
	public void apply( AccessoryHolder holder ) {
		if( holder.hasAnyBooster() ) {
			holder.removeBoosters();
		}
	}

	@Override
	public void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.translatable( "majruszsaccessories.cards.remove" ).withStyle( ChatFormatting.GRAY ) );
	}

	@Override
	public List< ItemStack > getCraftingRemainder( AccessoryHolder holder ) {
		return holder.getBoosters().stream().map( ItemStack::new ).toList();
	}
}
