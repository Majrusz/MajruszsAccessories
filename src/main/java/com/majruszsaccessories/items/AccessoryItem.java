package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.mlib.MajruszLibrary;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;

public class AccessoryItem extends Item {
	final String id;

	public AccessoryItem( String id ) {
		super( new Properties().tab( Registries.ITEM_GROUP ).stacksTo( 1 ) );

		this.id = id;
	}

	@Override
	public void onCraftedBy( ItemStack itemStack, Level level, Player player ) {
		AccessoryHandler handler = new AccessoryHandler( itemStack );
		if( handler.hasBonusRangeTag() ) {
			handler.applyBonusRange();
		}
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return new AccessoryHandler( itemStack ).hasMaxBonus();
	}

	@Override
	public Rarity getRarity( ItemStack itemStack ) {
		return new AccessoryHandler( itemStack ).getItemRarity();
	}

	@Override
	public void fillItemCategory( CreativeModeTab itemGroup, NonNullList< ItemStack > itemStacks ) {
		if( !this.allowdedIn( itemGroup ) )
			return;

		for( int i = 0; i < 9; ++i ) {
			float bonus = Math.round( 100.0f * Mth.lerp( i / 8.0f, AccessoryHandler.MIN_BONUS, AccessoryHandler.MAX_BONUS ) ) / 100.0f;
			itemStacks.add( AccessoryHandler.setup( new ItemStack( this ), bonus ).getItemStack() );
		}
	}

	public List< AccessoryModifier > getModifiers() {
		return MajruszLibrary.MOD_CONFIGS.get( this.id )
			.getConfigs()
			.stream()
			.filter( config->config instanceof AccessoryModifier )
			.map( config->( AccessoryModifier )config )
			.toList();
	}
}