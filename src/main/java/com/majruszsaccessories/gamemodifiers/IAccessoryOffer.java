package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.world.entity.npc.VillagerProfession;

public interface IAccessoryOffer {
	VillagerProfession getProfession();

	AccessoryItem getItem();

	int getTier();

	int getPrice();
}
