package com.majruszsaccessories.config;

import com.mlib.data.Serializable;
import com.mlib.math.Random;
import com.mlib.math.Range;
import net.minecraft.util.Mth;

public class Config extends com.mlib.data.Config {
	public Accessories accessories = new Accessories();
	public Boosters boosters = new Boosters();
	public Efficiency efficiency = new Efficiency();

	public Config( String name ) {
		super( name );

		this.defineCustom( "accessories", ()->this.accessories );
		this.defineCustom( "boosters", ()->this.boosters );
		this.defineCustom( "efficiency", ()->this.efficiency );
	}

	public static class Accessories extends Serializable {}

	public static class Boosters extends Serializable {}

	public static class Efficiency extends Serializable {
		public Range< Float > range = Range.of( -0.6f, 0.6f );
		public float avg = 0.0f;
		public float std = 0.2f;

		public Efficiency() {
			Range< Float > range = Range.of( -1.0f, 10.0f );

			this.defineFloatRange( "range", ()->this.range, x->this.range = range.clamp( x ) );
			this.defineFloat( "average", ()->this.avg, x->this.avg = range.clamp( x ) );
			this.defineFloat( "standard_deviation", ()->this.std, x->this.std = range.clamp( x ) );
		}

		public float getRandom() {
			if( Math.abs( this.range.to - this.range.from ) < 1e-5 ) {
				return this.range.from;
			}

			float ratio = ( float )( ( Random.nextGaussian() * this.std + this.avg - this.range.from ) / ( this.range.to - this.range.from ) );

			return this.range.lerp( Mth.clamp( ratio, 0.0f, 1.0f ) );
		}
	}
}
