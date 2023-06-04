package com.majruszsaccessories.boosters;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class BoosterItem extends Item {
	public BoosterItem() {
		super( new Properties().stacksTo( 1 ) );
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return true;
	}

	@Override
	public Rarity getRarity( ItemStack itemStack ) {
		return Rarity.RARE;
	}
}