package com.majruszsaccessories.contexts;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.math.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnAccessoryDropChanceGet {
	public final float original;
	public float chance;
	public final @Nullable Player player;

	public static Event< OnAccessoryDropChanceGet > listen( Consumer< OnAccessoryDropChanceGet > consumer ) {
		return Events.get( OnAccessoryDropChanceGet.class ).add( consumer );
	}

	public OnAccessoryDropChanceGet( float chance, @Nullable Entity entity ) {
		this.original = chance;
		this.chance = chance;
		this.player = entity instanceof Player player ? player : null;
	}

	public boolean check() {
		return Random.check( Mth.clamp( this.chance, 0.0f, 1.0f ) );
	}
}
