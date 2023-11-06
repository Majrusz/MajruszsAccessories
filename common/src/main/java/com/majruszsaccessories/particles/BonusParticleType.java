package com.majruszsaccessories.particles;

import com.majruszsaccessories.MajruszsAccessories;
import com.mlib.data.Serializables;
import com.mlib.particles.CustomParticleOptions;
import com.mlib.particles.CustomParticleType;

public class BonusParticleType extends CustomParticleType< BonusParticleType.Options > {
	static {
		Serializables.get( Options.class )
			.defineInteger( "color", s->s.color, ( s, v )->s.color = v );
	}

	public BonusParticleType() {
		super( Options.class, Options::new );
	}

	public static class Options extends CustomParticleOptions< Options > {
		public int color;

		public Options() {
			super( Options.class, MajruszsAccessories.BONUS_PARTICLE );
		}

		public Options( int color ) {
			this();

			this.color = color;
		}
	}
}
