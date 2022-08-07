package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class AccessoryItem extends Item {
	public AccessoryItem( String configName, String registryKey ) {
		super( new Properties().stacksTo( 1 ).rarity( Rarity.RARE ).tab( Registries.ITEM_GROUP ) );
	}

	@Override
	public void fillItemCategory( CreativeModeTab creativeTab, NonNullList< ItemStack > itemStacks ) {
		if( !this.allowedIn( creativeTab ) ) {
			return;
		}

		for( int i = 0; i < 9; ++i ) {
			itemStacks.add( AccessoryHandler.construct( this, i / 8.0f ) );
		}
	}
}