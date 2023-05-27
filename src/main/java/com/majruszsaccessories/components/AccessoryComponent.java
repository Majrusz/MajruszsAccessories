package com.majruszsaccessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.tooltip.ITooltipProvider;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AccessoryComponent {
	protected final Supplier< AccessoryItem > item;
	final List< ITooltipProvider > tooltipProviders = new ArrayList<>();

	public AccessoryComponent( Supplier< AccessoryItem > item ) {
		this.item = item;
	}

	public AccessoryComponent addTooltip( String key, ITooltipProvider... providers ) {
		this.tooltipProviders.add( new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				return new TranslatableComponent( key, Stream.of( providers ).map( provider->provider.getTooltip( holder ) ).toArray() );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				return new TranslatableComponent( key, Stream.of( providers ).map( provider->provider.getDetailedTooltip( holder ) ).toArray() );
			}
		} );

		return this;
	}

	public List< ITooltipProvider > getTooltipProviders() {
		return Collections.unmodifiableList( this.tooltipProviders );
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
	public interface ISupplier {
		AccessoryComponent accept( Supplier< AccessoryItem > item, ConfigGroup group );
	}
}
