package com.majruszsaccessories.mixininterfaces;

import com.majruszsaccessories.common.AccessoryHolder;

public interface IMixinItemStack {
	void majruszsaccessories$setAccessoryHolder( AccessoryHolder holder );

	AccessoryHolder majruszsaccessories$getOrCreateAccessoryHolder();
}
