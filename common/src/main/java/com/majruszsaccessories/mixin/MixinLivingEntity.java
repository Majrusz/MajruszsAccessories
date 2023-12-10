package com.majruszsaccessories.mixin;

import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity implements IMixinLivingEntity {
	AccessoryHolder majruszsaccessories$accessoryHolder = AccessoryHolder.create( ItemStack.EMPTY );

	@Override
	public void majruszsaccessories$setAccessoryHolder( AccessoryHolder holder ) {
		this.majruszsaccessories$accessoryHolder = holder;
	}

	@Override
	public AccessoryHolder majruszsaccessories$getAccessoryHolder() {
		return this.majruszsaccessories$accessoryHolder;
	}
}
