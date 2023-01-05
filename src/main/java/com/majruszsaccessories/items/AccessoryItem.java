package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.mlib.MajruszLibrary;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;

public class AccessoryItem extends Item {
	final String id;

	public AccessoryItem( String id ) {
		super( new Properties().stacksTo( 1 ) );

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

	public List< AccessoryModifier > getModifiers() {
		return MajruszLibrary.MOD_CONFIGS.get( this.id )
			.getConfigs()
			.stream()
			.filter( config->config instanceof AccessoryModifier )
			.map( config->( AccessoryModifier )config )
			.toList();
	}
}