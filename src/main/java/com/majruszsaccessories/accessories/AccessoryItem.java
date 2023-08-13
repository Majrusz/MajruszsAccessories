package com.majruszsaccessories.accessories;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AccessoryItem extends Item {
	public AccessoryItem() {
		super( new Properties().tab( Registries.ITEM_GROUP ).stacksTo( 1 ) );
	}

	@Override
	public void onCraftedBy( ItemStack itemStack, Level level, Player player ) {
		AccessoryHolder holder = AccessoryHolder.create( itemStack );
		if( holder.hasBonusRangeTag() && !holder.hasBonusTag() ) {
			holder.setRandomBonus();
		}
		if( holder.hasBoosterTag() && player instanceof ServerPlayer serverPlayer ) {
			Registries.HELPER.triggerAchievement( serverPlayer, "booster_used" );
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
	public ItemStack getCraftingRemainingItem( ItemStack itemStack ) {
		if( this.hasCraftingRemainingItem( itemStack ) ) {
			return new ItemStack( AccessoryHolder.create( itemStack ).getBooster() );
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public boolean hasCraftingRemainingItem( ItemStack itemStack ) {
		return AccessoryHolder.create( itemStack ).hasBoosterTag();
	}

	@Override
	public void fillItemCategory( CreativeModeTab itemGroup, NonNullList< ItemStack > itemStacks ) {
		if( !this.allowedIn( itemGroup ) )
			return;

		for( int i = 0; i < 9; ++i ) {
			float bonus = Math.round( 100.0f * Mth.lerp( i / 8.0f, AccessoryHolder.BONUS_RANGE.from, AccessoryHolder.BONUS_RANGE.to ) ) / 100.0f;
			itemStacks.add( AccessoryHolder.create( this ).setBonus( bonus ).getItemStack() );
		}
	}
}