package com.majruszsaccessories.items;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.mlib.item.CreativeModeTabHelper;
import com.mlib.registry.Registries;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CreativeModeTabs {
	private static final Component PRIMARY = TextHelper.translatable( "itemGroup.majruszsaccessories.primary" );

	public static Supplier< CreativeModeTab > primary() {
		CreativeModeTabHelper.createItemIconReplacer( CreativeModeTabs::getPrimaryIcons, PRIMARY );

		return ()->CreativeModeTab.builder( CreativeModeTab.Row.TOP, 0 )
			.title( PRIMARY )
			.displayItems( CreativeModeTabs::definePrimaryItems )
			.build();
	}

	private static List< Item > getPrimaryIcons() {
		List< Item > items = new ArrayList<>();
		for( Item item : Registries.getItems() ) {
			if( item instanceof AccessoryItem ) {
				items.add( item );
			}
		}

		return items;
	}

	private static void definePrimaryItems( CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output ) {
		for( Item item : Registries.getItems() ) {
			if( item instanceof AccessoryItem accessory ) {
				for( int i = 0; i < 9; ++i ) {
					float bonus = Mth.lerp( i / 8.0f, MajruszsAccessories.CONFIG.efficiency.range.from, MajruszsAccessories.CONFIG.efficiency.range.to );

					output.accept( AccessoryHolder.create( accessory ).setBonus( bonus ).getItemStack() );
				}
			}
		}

		Stream.of(
			MajruszsAccessories.DICE,
			MajruszsAccessories.GOLDEN_DICE,
			MajruszsAccessories.OWL_FEATHER,
			MajruszsAccessories.HORSESHOE,
			MajruszsAccessories.GOLDEN_HORSESHOE,
			MajruszsAccessories.ONYX
		).map( item->new ItemStack( item.get() ) ).forEach( output::accept );
	}
}
