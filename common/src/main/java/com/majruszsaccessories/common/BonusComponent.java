package com.majruszsaccessories.common;

import com.majruszsaccessories.tooltip.ITooltipProvider;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.level.LevelHelper;
import com.mlib.text.TextHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class BonusComponent< Type extends Item > {
	protected final BonusHandler< Type > handler;
	final List< ITooltipProvider > tooltipProviders = new ArrayList<>();

	public BonusComponent( BonusHandler< Type > handler ) {
		this.handler = handler;
	}

	public BonusComponent< Type > addTooltip( String key, ITooltipProvider... providers ) {
		this.tooltipProviders.add( new ITooltipProvider() {
			@Override
			public MutableComponent getTooltip( AccessoryHolder holder ) {
				return TextHelper.translatable( key, Stream.of( providers ).map( provider->provider.getTooltip( holder ) ).toArray() );
			}

			@Override
			public MutableComponent getDetailedTooltip( AccessoryHolder holder ) {
				return TextHelper.translatable( key, Stream.of( providers ).map( provider->provider.getDetailedTooltip( holder ) ).toArray() );
			}

			@Override
			public MutableComponent getRangeTooltip( AccessoryHolder holder ) {
				return TextHelper.translatable( key, Stream.of( providers ).map( provider->provider.getRangeTooltip( holder ) ).toArray() );
			}
		} );

		return this;
	}

	public List< ITooltipProvider > getTooltipProviders() {
		return Collections.unmodifiableList( this.tooltipProviders );
	}

	protected void addToGeneratedLoot( OnLootGenerated data ) {
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected void replaceGeneratedLoot( OnLootGenerated data ) {
		data.generatedLoot.clear();
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected void spawnFlyingItem( Level level, Vec3 from, Vec3 to ) {
		LevelHelper.spawnItemEntityFlyingTowardsDirection( this.constructItemStack(), level, from, to );
	}

	protected ItemStack constructItemStack() {
		AccessoryHolder holder = AccessoryHolder.create( this.handler.getItem() );
		if( holder.isValid() ) {
			holder.setRandomBonus();
		}

		return holder.getItemStack();
	}


	protected Type getItem() {
		return this.handler.getItem();
	}

	@FunctionalInterface
	public interface ISupplier< Type extends Item > {
		BonusComponent< Type > apply( BonusHandler< Type > handler );
	}
}
