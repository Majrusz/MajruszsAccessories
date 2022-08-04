package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class AccessoryItem extends Item {
	public AccessoryItem( String configName, String registryKey ) {
		super( new Properties().stacksTo( 1 ).rarity( Rarity.RARE ).tab( Registries.ITEM_GROUP ) );
	}

	public static double getBonus( ItemStack itemStack ) {
		return itemStack.getItem() instanceof AccessoryItem ? itemStack.getOrCreateTagElement( Tags.BONUS ).getDouble( Tags.VALUE ) : 0.0;
	}

	public static boolean hasBonusTag( ItemStack itemStack ) {
		return itemStack.getTagElement( Tags.BONUS ) != null;
	}

	static final class Tooltips {
		static final String INVENTORY = "majruszsaccessories.items.accessory_item";
		static final String BONUS = "majruszsaccessories.items.bonus";
	}

	static final class Tags {
		static final String BONUS = "Bonus";
		static final String VALUE = "Value";
	}
}