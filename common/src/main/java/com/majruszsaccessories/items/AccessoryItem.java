package com.majruszsaccessories.items;

import com.majruszsaccessories.common.AccessoryHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class AccessoryItem extends Item {
	public AccessoryItem() {
		super( new Properties().stacksTo( 1 ) );
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return AccessoryHolder.create( itemStack ).hasMaxBonus();
	}

	@Override
	public Rarity getRarity( ItemStack itemStack ) {
		return AccessoryHolder.create( itemStack ).getRarity();
	}
}