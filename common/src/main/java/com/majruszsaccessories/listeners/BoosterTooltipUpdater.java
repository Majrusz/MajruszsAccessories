package com.majruszsaccessories.listeners;

import com.majruszsaccessories.items.BoosterItem;
import com.majruszsaccessories.contexts.OnBoosterTooltip;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemTooltip;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Contexts;
import com.mlib.text.TextHelper;
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

	private void addTooltip( OnItemTooltip data ) {
		List< Component > components = new ArrayList<>();
		components.addAll( this.buildGenericInfo() );
		components.addAll( this.buildEffectsInfo( data ) );

		data.components.addAll( 1, components );
	}

	private List< Component > buildGenericInfo() {
		return List.of( TextHelper.translatable( Tooltips.INFO ).withStyle( ChatFormatting.GOLD ) );
	}

	private List< Component > buildEffectsInfo( OnItemTooltip data ) {
		return Contexts.dispatch( new OnBoosterTooltip( ( BoosterItem )data.itemStack.getItem() ) ).components;
	}

	static final class Tooltips {
		static final String INFO = "majruszsaccessories.items.booster_tooltip";
	}
}
