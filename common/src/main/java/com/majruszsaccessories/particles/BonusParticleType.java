package com.majruszsaccessories.particles;

import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.particles.CustomParticleOptions;
import com.majruszlibrary.particles.CustomParticleType;
import com.majruszsaccessories.MajruszsAccessories;

public class BonusParticleType extends CustomParticleType< BonusParticleType.Options > {
	static {
		Serializables.get( Options.class )
			.define( "color", Reader.integer(), s->s.color, ( s, v )->s.color = v );
	}

	public BonusParticleType() {
		super( Options::new );
	}

	public static class Options extends CustomParticleOptions< Options > {
		public int color;

		public Options() {
			super( MajruszsAccessories.BONUS_PARTICLE );
		}

		public Options( int color ) {
			this();

			this.color = color;
		}
	}
}
