package com.majruszsaccessories.accessories;

import com.majruszsaccessories.AccessoryHolder;
import net.minecraft.world.entity.player.Player;
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
}