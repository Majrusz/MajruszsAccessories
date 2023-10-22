package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.common.Component;
import com.majruszsaccessories.common.Handler;
import com.mlib.level.LevelHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AccessoryComponent extends Component< AccessoryItem > {
	public AccessoryComponent( Handler< AccessoryItem > handler ) {
		super( handler );
	}

	@Override
	protected ItemStack constructItemStack() {
		return AccessoryHolder.create( this.handler.getItem() ).setRandomBonus().getItemStack();
	}

	protected void spawnFlyingItem( Level level, Vec3 from, Vec3 to ) {
		LevelHelper.spawnItemEntityFlyingTowardsDirection( this.constructItemStack(), level, from, to );
	}
}
