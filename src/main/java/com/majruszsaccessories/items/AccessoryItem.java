package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.IRegistrable;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AccessoryItem extends Item implements IRegistrable {
	public static final List< GameModifiersHolder< ? extends AccessoryItem > > ACCESSORIES = new ArrayList<>();
	GameModifiersHolder< ? > holder = null;

	public AccessoryItem() {
		super( new Properties().stacksTo( 1 ).rarity( Rarity.RARE ).tab( Registries.ITEM_GROUP ) );
	}

	@Override
	public void fillItemCategory( CreativeModeTab creativeTab, NonNullList< ItemStack > itemStacks ) {
		if( !this.allowedIn( creativeTab ) ) {
			return;
		}

		for( int i = 0; i < 9; ++i ) {
			float bonus = Math.round( 100.0f * Mth.lerp( i / 8.0f, AccessoryHandler.MIN_BONUS, AccessoryHandler.MAX_BONUS ) ) / 100.0f;
			itemStacks.add( AccessoryHandler.construct( this, bonus ) );
		}
	}

	@Override
	public void setHolder( GameModifiersHolder< ? > holder ) {
		this.holder = holder;
	}

	@Override
	public GameModifiersHolder< ? > getHolder() {
		return this.holder;
	}

	protected static < Type extends AccessoryItem > GameModifiersHolder< Type > newHolder( String configKey, Supplier< Type > supplier ) {
		GameModifiersHolder< Type > holder = new GameModifiersHolder<>( configKey, supplier );
		ACCESSORIES.add( holder );

		return holder;
	}
}