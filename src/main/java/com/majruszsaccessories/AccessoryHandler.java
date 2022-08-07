package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItem;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.Optional;

public class AccessoryHandler {
	public static final float MIN_BONUS = -0.6f, MAX_BONUS = 0.6f;
	final ItemStack itemStack;
	final AccessoryItem item;

	public AccessoryHandler( ItemStack itemStack ) {
		assert itemStack.getItem() instanceof AccessoryItem;

		this.itemStack = itemStack;
		this.item = ( AccessoryItem )itemStack.getItem();
	}

	public static void setup( ItemStack itemStack, float ratio ) {
		AccessoryHandler handler = new AccessoryHandler( itemStack );
		if( handler.hasBonusTag() ) {
			return;
		}

		handler.setBonus( ratio );
	}

	public static ItemStack construct( AccessoryItem item, float ratio ) {
		ItemStack itemStack = new ItemStack( item );
		setup( itemStack, ratio );

		return itemStack;
	}

	public static float ratioToBonus( float ratio ) {
		return Mth.lerp( ratio, MIN_BONUS, MAX_BONUS );
	}

	public boolean hasAccessory( Player player ) {
		return getAccessory( player ) != null;
	}

	@Nullable
	public ItemStack getAccessory( Player player ) {
		if( Integration.isCuriosInstalled() ) {
			Optional< SlotResult > slotResult = CuriosApi.getCuriosHelper().findFirstCurio( player, this.item );
			if( slotResult.isPresent() ) {
				return slotResult.get().stack();
			}
		} else {
			ItemStack itemStack = player.getOffhandItem();
			if( itemStack.getItem() instanceof AccessoryItem ) {
				return itemStack;
			}
		}

		return null;
	}

	public void setBonus( float ratio ) {
		assert 0.0 <= ratio && ratio <= 1.0;

		this.itemStack.getOrCreateTagElement( Tags.BONUS ).putFloat( Tags.VALUE, ratioToBonus( ratio ) );
	}

	public float getBonus() {
		return this.itemStack.getOrCreateTagElement( Tags.BONUS ).getFloat( Tags.VALUE );
	}

	public boolean hasBonusTag() {
		return this.itemStack.getTagElement( Tags.BONUS ) != null;
	}

	static final class Tags {
		static final String BONUS = "Bonus";
		static final String VALUE = "Value";
	}
}