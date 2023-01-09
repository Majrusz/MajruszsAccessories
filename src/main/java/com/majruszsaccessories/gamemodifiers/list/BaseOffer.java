package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.IAccessoryOffer;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.config.IntegerConfig;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BaseOffer extends AccessoryModifier implements IAccessoryOffer {
	public static List< BaseOffer > OFFERS = new ArrayList<>();
	final IntegerConfig price;
	final VillagerProfession profession;
	final int tier;

	public BaseOffer( Supplier< ? extends AccessoryItem > item, String configKey, VillagerProfession profession, int tier, int price ) {
		super( item, configKey );

		this.price = new IntegerConfig( price, new Range<>( 1, 32 ) );
		this.profession = profession;
		this.tier = tier;

		this.addConfig( this.price.name( "trade_price" ).comment( "Price the villager will pay for this accessory." ).requiresWorldRestart( true ) );

		OFFERS.add( this );
	}

	public BaseOffer( Supplier< ? extends AccessoryItem > item, String configKey, VillagerProfession profession, int tier ) {
		this( item, configKey, profession, tier, 7 );
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
