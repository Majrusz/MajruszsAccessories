package com.majruszsaccessories.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BoosterOverlay extends Item {
	public BoosterOverlay() {
		super( new Item.Properties().stacksTo( 1 ) );
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return true;
	}
}
