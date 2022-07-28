package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AccessoryItem extends Item {
	public AccessoryItem( String configName, String registryKey ) {
		super( new Properties().stacksTo( 1 ).rarity( Rarity.RARE ).tab( Registries.ITEM_GROUP ) );
	}
}