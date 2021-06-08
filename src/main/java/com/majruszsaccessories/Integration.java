package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/** Class that handles integration with optional dependencies. */
public class Integration {
	private static boolean IS_CURIOS_INSTALLED;

	/** Initializes all required information about optional dependencies. */
	public static void initialize() {
		ModList modList = ModList.get();

		IS_CURIOS_INSTALLED = modList.isLoaded( "curios" );
	}

	/** Returns whether Curios mod is installed. */
	public static boolean isCuriosInstalled() {
		return IS_CURIOS_INSTALLED;
	}
}
