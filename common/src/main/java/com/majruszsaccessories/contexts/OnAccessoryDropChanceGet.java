package com.majruszsaccessories.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.math.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnAccessoryDropChanceGet {
	public final float original;
	public float chance;
	public final @Nullable Player player;

	public static Context< OnAccessoryDropChanceGet > listen( Consumer< OnAccessoryDropChanceGet > consumer ) {
		return Contexts.get( OnAccessoryDropChanceGet.class ).add( consumer );
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
