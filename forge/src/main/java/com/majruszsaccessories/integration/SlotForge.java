package com.majruszsaccessories.integration;

import com.majruszlibrary.platform.Integration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

public class SlotForge implements ISlotPlatform {
	@Override
	public boolean isInstalled() {
		return Integration.isLoaded( "curios" );
	}

	@Override
	public ItemStack find( LivingEntity entity, Predicate< ItemStack > predicate ) {
		Optional< SlotResult > slotResult = CuriosApi.getCuriosHelper().findFirstCurio( entity, predicate );
		if( slotResult.isPresent() ) {
			return slotResult.get().stack();
		}

		return ItemStack.EMPTY;
	}
}
