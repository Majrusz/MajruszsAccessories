package com.majruszsaccessories.items;

import com.majruszlibrary.events.OnItemTooltip;
import com.majruszsaccessories.common.AccessoryHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public abstract class CardItem extends Item {
	static {
		OnItemTooltip.listen( CardItem::tryToAddTooltip );
	}

	public CardItem() {
		super( new Properties().rarity( Rarity.UNCOMMON ).stacksTo( 8 ) );
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return true;
	}

	public abstract void apply( AccessoryHolder holder );

	public abstract void addTooltip( OnItemTooltip data );

	private static void tryToAddTooltip( OnItemTooltip data ) {
		if( data.itemStack.getItem() instanceof CardItem card ) {
			card.addTooltip( data );
		}
	}
}
