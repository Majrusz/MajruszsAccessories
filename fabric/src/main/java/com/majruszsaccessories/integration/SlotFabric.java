package com.majruszsaccessories.integration;

import com.majruszlibrary.platform.Integration;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SlotFabric implements ISlotPlatform {
	@Override
	public boolean isInstalled() {
		return Integration.isLoaded( "trinkets" );
	}

	@Override
	public List< ItemStack > find( LivingEntity entity, Predicate< ItemStack > predicate ) {
		Optional< TrinketComponent > component = TrinketsApi.getTrinketComponent( entity );
		if( component.isPresent() ) {
			return component.get().getEquipped( predicate ).stream().map( Tuple::getB ).toList();
		}

		return List.of();
	}
}
