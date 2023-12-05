package com.majruszsaccessories.config;

import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import net.minecraft.util.Mth;

public class Config {
	static {
		Serializables.getStatic( Config.class )
			.define( "accessories", Accessories.class )
			.define( "boosters", Boosters.class )
			.define( "efficiency", Efficiency.class );

		Serializables.getStatic( Accessories.class );

		Serializables.getStatic( Boosters.class );

		Serializables.getStatic( Efficiency.class )
			.define( "range", Reader.range( Reader.number() ), ()->Efficiency.RANGE, v->Efficiency.RANGE = Range.of( -1.0f, 10.0f ).clamp( v ) )
			.define( "average", Reader.number(), ()->Efficiency.AVG, v->Efficiency.AVG = Range.of( -1.0f, 10.0f ).clamp( v ) )
			.define( "standard_deviation", Reader.number(), ()->Efficiency.STD, v->Efficiency.STD = Range.of( -1.0f, 10.0f ).clamp( v ) );
	}

	public static class Accessories {}

	public static class Boosters {}

	public static class Efficiency {
		public static Range< Float > RANGE = Range.of( -0.6f, 0.6f );
		public static float AVG = 0.0f;
		public static float STD = 0.2f;

		public static float getRandom() {
			if( Math.abs( RANGE.to - RANGE.from ) < 1e-5 ) {
				return RANGE.from;
			}

			return RANGE.lerp( Efficiency.getGaussianRatio() );
		}

		public static float getGaussianRatio() {
			return Mth.clamp( ( float )( ( Random.nextGaussian() * STD + AVG - RANGE.from ) / ( RANGE.to - RANGE.from ) ), 0.0f, 1.0f );
		}
	}
}
