package com.majruszsaccessories.accessories.tooltip;

import com.majruszsaccessories.AccessoryHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface ITooltipProvider {
	MutableComponent getTooltip( AccessoryHolder holder );

	default MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
		return this.getTooltip( holder );
	}
}
