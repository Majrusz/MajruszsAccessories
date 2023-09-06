package com.majruszsaccessories.tooltip;

import com.majruszsaccessories.accessories.AccessoryHolder;
import net.minecraft.network.chat.MutableComponent;

public interface ITooltipProvider {
	MutableComponent getTooltip( AccessoryHolder holder );

	default MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
		return this.getTooltip( holder );
	}
}
