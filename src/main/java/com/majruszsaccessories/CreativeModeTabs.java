package com.majruszsaccessories;

import com.mlib.annotations.AutoInstance;
import com.mlib.items.CreativeModeTabHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;
import java.util.stream.Stream;

@AutoInstance
public class CreativeModeTabs {
	public CreativeModeTabs() {
		var primary = CreativeModeTabHelper.newTab( FMLJavaModLoadingContext.get(), Registries.getLocation( "primary" ) );
		primary.accept( builder->builder.title( Component.translatable( "itemGroup.majruszsaccessories.primary" ) )
			.displayItems( this::definePrimaryItems )
			.withTabFactory( Primary::new )
		);
	}

	private void definePrimaryItems( FeatureFlagSet flagSet, CreativeModeTab.Output output, boolean hasPermissions ) {
		Stream.of(
			// TIER I
			Registries.ADVENTURERS_GUIDE,
			Registries.ANGLERS_TROPHY,
			Registries.CERTIFICATE_OF_TAMING,
			Registries.IDOL_OF_FERTILITY,
			Registries.LUCKY_ROCK,
			Registries.SECRET_INGREDIENT,
			Registries.TAMED_POTATO_BEETLE,
			Registries.WHITE_FLAG,
			// TIER II
			Registries.OVERWORLD_RUNE
		).forEach( item->{
			for( int i = 0; i < 9; ++i ) {
				float bonus = Math.round( 100.0f * Mth.lerp( i / 8.0f, AccessoryHolder.BONUS_RANGE.from, AccessoryHolder.BONUS_RANGE.to ) ) / 100.0f;
				output.accept( AccessoryHolder.create( item.get() ).setBonus( bonus ).getItemStack() );
			}
		} );

		Stream.of(
			Registries.DICE,
			Registries.GOLDEN_DICE,
			Registries.OWL_FEATHER,
			Registries.GOLDEN_HORSESHOE
		).forEach( item->output.accept( new ItemStack( item.get() ) ) );
	}

	private static class Primary extends CreativeModeTab {
		final Supplier< ItemStack > currentIcon;

		protected Primary( Builder builder ) {
			super( builder );

			this.currentIcon = CreativeModeTabHelper.buildMultiIcon( Stream.of(
				Registries.ADVENTURERS_GUIDE,
				Registries.ANGLERS_TROPHY,
				Registries.CERTIFICATE_OF_TAMING,
				Registries.IDOL_OF_FERTILITY,
				Registries.LUCKY_ROCK,
				Registries.OVERWORLD_RUNE,
				Registries.SECRET_INGREDIENT,
				Registries.TAMED_POTATO_BEETLE,
				Registries.WHITE_FLAG
			) );
		}

		@Override
		public ItemStack getIconItem() {
			return this.currentIcon.get();
		}
	}
}
