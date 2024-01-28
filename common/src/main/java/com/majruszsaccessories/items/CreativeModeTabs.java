package com.majruszsaccessories.items;

import com.majruszlibrary.registry.Registries;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CreativeModeTabs {
	public static final Component PRIMARY = TextHelper.translatable( "itemGroup.majruszsaccessories.primary" );

	public static List< Item > getPrimaryIcons() {
		List< Item > items = new ArrayList<>();
		for( Item item : Registries.ITEMS ) {
			if( item instanceof AccessoryItem ) {
				items.add( item );
			}
		}

		return items;
	}

	public static void definePrimaryItems( Consumer< ItemStack > output ) {
		List< AccessoryItem > accessories = new ArrayList<>();
		for( Item item : Registries.ITEMS ) {
			if( item instanceof AccessoryItem accessory ) {
				accessories.add( accessory );
			}
		}
		accessories.sort( Comparator.comparingInt( AccessoryItem::getBoosterSlotsCount ) );
		for( AccessoryItem accessory : accessories ) {
			for( int i = 0; i < 9; ++i ) {
				float bonus = Mth.lerp( i / 8.0f, Config.Efficiency.RANGE.from, Config.Efficiency.RANGE.to );

				output.accept( AccessoryHolder.create( accessory ).setBonus( bonus ).getItemStack() );
			}
		}

		Stream.of(
			MajruszsAccessories.OWL_FEATHER,
			MajruszsAccessories.DICE,
			MajruszsAccessories.GOLDEN_DICE,
			MajruszsAccessories.ONYX,
			MajruszsAccessories.HORSESHOE,
			MajruszsAccessories.GOLDEN_HORSESHOE,
			MajruszsAccessories.GAMBLING_CARD,
			MajruszsAccessories.REMOVAL_CARD,
			MajruszsAccessories.REVERSE_CARD
		).map( item->new ItemStack( item.get() ) ).forEach( output::accept );
	}
}
