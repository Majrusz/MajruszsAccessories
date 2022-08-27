package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.IAccessoryOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.config.IntegerConfig;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

public class BaseOffer extends AccessoryModifier implements IAccessoryOffer {
	final IntegerConfig price = new IntegerConfig( "trade_price", "Price the villager will pay for this accessory.", true, 7, 1, 32 );
	final VillagerProfession profession;
	final int tier;

	public BaseOffer( Supplier< ? extends AccessoryItem > item, String configKey, VillagerProfession profession, int tier ) {
		super( item, configKey, "", "" );
		this.profession = profession;
		this.tier = tier;

		this.addConfig( this.price );
	}

	@Override
	public VillagerProfession getProfession() {
		return this.profession;
	}

	@Override
	public AccessoryItem getItem() {
		return this.item.get();
	}

	@Override
	public int getTier() {
		return this.tier;
	}

	@Override
	public int getPrice() {
		return this.price.get();
	}
}
