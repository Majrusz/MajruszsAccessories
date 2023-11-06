package com.majruszsaccessories.particles;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.client.CustomParticle;
import com.mlib.math.Random;
import com.mlib.time.TimeHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

@OnlyIn( Dist.CLIENT )
public class BonusParticle extends CustomParticle {
	private final SpriteSet spriteSet;

	public BonusParticle( ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet ) {
		super( level, x, y, z, xSpeed, ySpeed, zSpeed );

		this.spriteSet = spriteSet;
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
		this.xdFormula = xd->xd * 0.8;
		this.ydFormula = yd->yd * 0.8;
		this.zdFormula = zd->zd * 0.8;
		this.lifetime = TimeHelper.toTicks( Random.nextFloat( 1.8f, 2.4f ) );
		this.age = 0;
		this.scaleFormula = lifetime->0.5f;

		this.setSpriteFromAge( this.spriteSet );
	}

	public void update( BonusParticleType.Options options ) {
		int color = options.color;
		float colorRatio = Random.nextFloat( 0.8f, 1.0f );
		this.setColor( ( color >> 16 & 0xff ) / 255.0f * colorRatio, ( color >> 8 & 0xff ) / 255.0f * colorRatio, ( color & 0xff ) / 255.0f * colorRatio );
	}

	@Override
	public void tick() {
		super.tick();

		if( !this.removed ) {
			this.setSprite( this.spriteSet.get( Math.min( 8 * this.age, this.lifetime ), this.lifetime ) );
		}
		this.setAlpha( Math.min( 1.0f, 3.0f * ( 1.0f - ( float )this.age / this.lifetime ) ) );
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn( Dist.CLIENT )
	public static class Factory extends CustomParticle.Factory< BonusParticle, BonusParticleType.Options > {
		public Factory( SpriteSet sprite ) {
			super( sprite, BonusParticle::new, BonusParticle::update );
		}
	}
}
