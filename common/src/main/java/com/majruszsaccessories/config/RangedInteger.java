package com.majruszsaccessories.config;

import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.SerializableObject;
import com.majruszlibrary.math.Range;

public class RangedInteger {
	private Integer value;
	private Range< Integer > range;
	private String id = "value";
	private Range< Integer > maxRange = null;

	public RangedInteger id( String id ) {
		this.id = id;

		return this;
	}

	public RangedInteger maxRange( Range< Integer > range ) {
		this.maxRange = range;

		return this;
	}

	public void set( int value, Range< Integer > range ) {
		this.value = value;
		this.range = range;
	}

	public void define( SerializableObject< ? > serializable ) {
		serializable.define( this.id, Reader.integer(), s->this.value, ( s, v )->this.value = this.range.clamp( v ) );
		if( this.maxRange != null ) {
			serializable.define( "%s_range".formatted( this.id ), Reader.range( Reader.integer() ), s->this.range, ( s, v )->this.range = this.maxRange.clamp( v ) );
		}
	}

	public int get() {
		return this.value;
	}

	public Range< Integer > getRange() {
		return this.range;
	}
}
