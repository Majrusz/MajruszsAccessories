package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/** Custom creative mode item tab for accessories. */
public class AccessoryItemGroup extends ItemGroup {
	public AccessoryItemGroup( String label ) {
		super( label );
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack( Instances.FISHERMAN_EMBLEM_ITEM );
	}
}
