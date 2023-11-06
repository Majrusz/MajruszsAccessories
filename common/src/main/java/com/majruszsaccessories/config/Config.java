package com.majruszsaccessories.config;

import com.mlib.data.Serializables;
import com.mlib.math.Random;
import com.mlib.math.Range;
import net.minecraft.util.Mth;

public class Config extends com.mlib.data.Config {
	public Accessories accessories = new Accessories();
	public Boosters boosters = new Boosters();
	public Efficiency efficiency = new Efficiency();

	public Config( String name ) {
		super( name );

		Serializables.get( Config.class )
			.defineCustom( "accessories", ()->this.accessories )
			.defineCustom( "boosters", ()->this.boosters )
			.defineCustom( "efficiency", ()->this.efficiency );
	}

	public static class Accessories {}

	public static class Boosters {}

	public static class Efficiency {
		static {
			Serializables.get( Efficiency.class )
				.defineFloatRange( "range", s->s.range, ( s, v )->s.range = Range.of( -1.0f, 10.0f ).clamp( v ) )
				.defineFloat( "average", s->s.avg, ( s, v )->s.avg = Range.of( -1.0f, 10.0f ).clamp( v ) )
				.defineFloat( "standard_deviation", s->s.std, ( s, v )->s.std = Range.of( -1.0f, 10.0f ).clamp( v ) );
		}

		public Range< Float > range = Range.of( -0.6f, 0.6f );
		public float avg = 0.0f;
		public float std = 0.2f;

		public float getRandom() {
			if( Math.abs( this.range.to - this.range.from ) < 1e-5 ) {
				return this.range.from;
			}

			float ratio = ( float )( ( Random.nextGaussian() * this.std + this.avg - this.range.from ) / ( this.range.to - this.range.from ) );

			return this.range.lerp( Mth.clamp( ratio, 0.0f, 1.0f ) );
		}
	}
}
