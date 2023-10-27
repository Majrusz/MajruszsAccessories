package com.majruszsaccessories.integration;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface ISlotPlatform {
	boolean isInstalled();

	ItemStack find( LivingEntity entity, Predicate< ItemStack > predicate );
}

