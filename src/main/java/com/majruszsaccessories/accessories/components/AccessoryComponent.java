package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.common.ComponentBase;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AccessoryComponent extends ComponentBase< AccessoryItem > {
	public AccessoryComponent( Supplier< AccessoryItem > item ) {
		super( item );
	}

	protected void addToGeneratedLoot( OnLoot.Data data ) {
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected void spawnFlyingItem( Level level, Vec3 from, Vec3 to ) {
		LevelHelper.spawnItemEntityFlyingTowardsDirection( this.constructItemStack(), level, from, to );
	}

	protected ItemStack constructItemStack() {
		return AccessoryHolder.create( this.item.get() ).setRandomBonus().getItemStack();
	}

	@FunctionalInterface
	public interface ISupplier extends BiFunction< Supplier< AccessoryItem >, ConfigGroup, AccessoryComponent > {}
}
