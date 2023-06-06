package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.gamemodifiers.contexts.OnBoosterTooltip;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnItemTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@AutoInstance
public class BoosterTooltipUpdater {
	public BoosterTooltipUpdater() {
		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.itemStack.getItem() instanceof BoosterItem ) );
	}

	private void addTooltip( OnItemTooltip.Data data ) {
		List< Component > components = new ArrayList<>();
		components.addAll( this.buildGenericInfo() );
		components.addAll( this.buildEffectsInfo( data ) );

		data.tooltip.addAll( 1, components );
	}

	private List< Component > buildGenericInfo() {
		return List.of( Component.translatable( Tooltips.INFO ).withStyle( ChatFormatting.GOLD ) );
	}

	private List< Component > buildEffectsInfo( OnItemTooltip.Data data ) {
		return OnBoosterTooltip.dispatch( ( BoosterItem )data.itemStack.getItem() ).components;
	}

	static final class Tooltips {
		static final String INFO = "majruszsaccessories.items.booster_tooltip";
	}
}
