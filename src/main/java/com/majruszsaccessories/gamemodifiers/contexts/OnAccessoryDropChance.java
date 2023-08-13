package com.majruszsaccessories.gamemodifiers.contexts;

import com.mlib.Random;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnAccessoryDropChance {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( double chance, @Nullable Entity entity ) {
		return Contexts.get( Data.class ).dispatch( new Data( chance, entity ) );
	}

	public static class Data {
		public final double original;
		public double chance;
		@Nullable
		public final Player player;

		public Data( double chance, @Nullable Entity entity ) {
			this.original = this.chance = chance;
			this.player = entity instanceof Player player ? player : null;
		}

		public boolean tryChance() {
			return Random.tryChance( Mth.clamp( this.chance, 0.0, 1.0 ) );
		}
	}
}
