package com.majruszsaccessories.mixin;

import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity implements IMixinLivingEntity {
	AccessoryHolders majruszsaccessories$accessoryHolders = AccessoryHolders.EMPTY;

	public void majruszsaccessories$setAccessoryHolders( AccessoryHolders holders ) {
		this.majruszsaccessories$accessoryHolders = holders;
	}

	@Override
	public AccessoryHolders majruszsaccessories$getAccessoryHolders() {
		return this.majruszsaccessories$accessoryHolders;
	}
}
