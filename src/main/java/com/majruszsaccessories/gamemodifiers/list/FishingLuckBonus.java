package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.config.IntegerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.Supplier;

public class FishingLuckBonus extends AccessoryModifier {
	static final String BONUS_KEY = "majruszsaccessories.bonuses.fishing_luck";
	final IntegerConfig luck = new IntegerConfig( "fishing_luck", "Luck bonus during fishing.", false, 3, 1, 10 );

	public FishingLuckBonus( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( configKey, "", "" );

		this.addConfig( this.luck );
	}

	@Override
	public void addTooltip( List< MutableComponent > components, AccessoryHandler handler ) {
		components.add( Component.translatable( BONUS_KEY, this.luck.get() ) );
	}
}
