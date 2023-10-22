package com.majruszsaccessories.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class BoosterItem extends Item {
	final Rarity rarity;

	public static Supplier< BoosterItem > basic() {
		return ()->new BoosterItem( Rarity.UNCOMMON );
	}

	public static Supplier< BoosterItem > rare() {
		return ()->new BoosterItem( Rarity.RARE );
	}

	private BoosterItem( Rarity rarity ) {
		super( new Properties().stacksTo( 1 ) );

		this.rarity = rarity;
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return true;
	}

	@Override
	public Rarity getRarity( ItemStack itemStack ) {
		return this.rarity;
	}
}