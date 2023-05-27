package com.majruszsaccessories.items;

import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import com.majruszsaccessories.AccessoryHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AccessoryItem extends Item {
	public AccessoryItem() {
		super( new Properties().stacksTo( 1 ) );
	}

	@Override
	public void onCraftedBy( ItemStack itemStack, Level level, Player player ) {
		AccessoryHolder holder = AccessoryHolder.create( itemStack );
		if( holder.hasBonusRangeTag() && !holder.hasBonusTag() ) {
			holder.setRandomBonus();
		}
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return AccessoryHolder.create( itemStack ).hasMaxBonus();
	}

	@Override
	public Rarity getRarity( ItemStack itemStack ) {
		return AccessoryHolder.create( itemStack ).getItemRarity();
	}

	@Override
	public void fillItemCategory( CreativeModeTab itemGroup, NonNullList< ItemStack > itemStacks ) {
		if( !this.allowdedIn( itemGroup ) )
			return;

		for( int i = 0; i < 9; ++i ) {
			float bonus = Math.round( 100.0f * Mth.lerp( i / 8.0f, AccessoryHolder.BONUS_RANGE.from, AccessoryHolder.BONUS_RANGE.to ) ) / 100.0f;
			itemStacks.add( AccessoryHolder.create( this ).setBonus( bonus ).getItemStack() );
		}
	}
}