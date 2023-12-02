package com.majruszsaccessories.config;

import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.SerializableObject;
import com.majruszlibrary.math.Range;

public class RangedFloat {
	private float value;
	private Range< Float > range;
	private String id = "value";
	private Range< Float > maxRange = null;

	public RangedFloat id( String id ) {
		this.id = id;

		return this;
	}

	public RangedFloat maxRange( Range< Float > range ) {
		this.maxRange = range;

		return this;
	}

	public void set( float value, Range< Float > range ) {
		this.value = value;
		this.range = range;
	}

	public void define( SerializableObject< ? > serializable ) {
		serializable.define( this.id, Reader.number(), s->this.value, ( s, v )->this.value = this.range.clamp( v ) );
		if( this.maxRange != null ) {
			serializable.define( "%s_range".formatted( this.id ), Reader.range( Reader.number() ), s->this.range, ( s, v )->this.range = this.maxRange.clamp( v ) );
		}
	}

	public float get() {
		return this.value;
	}

	public Range< Float > getRange() {
		return this.range;
	}
}
