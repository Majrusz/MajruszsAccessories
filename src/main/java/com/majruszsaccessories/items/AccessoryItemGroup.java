package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/** Custom creative mode item tab for accessories. */
public class AccessoryItemGroup extends CreativeModeTab {
	public AccessoryItemGroup( String label ) {
		super( label );
	}

	/** Returns item stack that is a group icon. */
	@Override
	public ItemStack makeIcon() {
		return new ItemStack( Instances.FISHERMAN_EMBLEM_ITEM );
	}
}
