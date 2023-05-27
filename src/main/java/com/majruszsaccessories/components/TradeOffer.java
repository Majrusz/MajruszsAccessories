package com.majruszsaccessories.components;

import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.config.ConfigGroup;
import com.mlib.config.IntegerConfig;
import com.mlib.math.Range;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

public class TradeOffer extends AccessoryComponent {
	final IntegerConfig price;
	final VillagerProfession profession;
	final int tier;

	public static AccessoryComponent.ISupplier create( VillagerProfession profession, int tier, int price ) {
		return ( item, group )->new TradeOffer( item, group, profession, tier, price );
	}

	public static AccessoryComponent.ISupplier create( VillagerProfession profession, int tier ) {
		return create( profession, tier, 7 );
	}

	protected TradeOffer( Supplier< AccessoryItem > item, ConfigGroup group, VillagerProfession profession, int tier, int price ) {
		super( item );

		this.price = new IntegerConfig( price, new Range<>( 1, 32 ) );
		this.profession = profession;
		this.tier = tier;

		group.addConfig( this.price.name( "trade_price" ).comment( "Price the villager will pay for this accessory." ).requiresWorldRestart( true ) );
	}

	public VillagerProfession getProfession() {
		return this.profession;
	}

	public AccessoryItem getItem() {
		return this.item.get();
	}

	public int getTier() {
		return this.tier;
	}

	public int getPrice() {
		return this.price.get();
	}
}
