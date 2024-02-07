package com.majruszsaccessories.mixin;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.mixininterfaces.IMixinItemStack;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( ItemStack.class )
public abstract class MixinItemStack implements IMixinItemStack {
	AccessoryHolder majruszsaccessories$accessoryHolder = null;

	@Override
	public void majruszsaccessories$setAccessoryHolder( AccessoryHolder holder ) {
		this.majruszsaccessories$accessoryHolder = holder;
	}

	@Override
	public AccessoryHolder majruszsaccessories$getOrCreateAccessoryHolder() {
		if( this.majruszsaccessories$accessoryHolder == null ) {
			AccessoryHolder.create( ( ItemStack )( Object )this );
		}

		return this.majruszsaccessories$accessoryHolder;
	}
}
