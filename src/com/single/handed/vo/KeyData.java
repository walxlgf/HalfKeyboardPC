package com.single.handed.vo;

import java.io.Serializable;

public class KeyData implements Serializable {
	private static final long serialVersionUID = 2L;
	public static final int TYPE_KEY_PRESS = 1;
	public static final int TYPE_KEY_RELEASE = 2;
	private int type;
	private int code;

	
	public KeyData() {
		super();
	}

	public KeyData(int code, int type) {
		super();
		this.type = type;
		this.code = code;
	}

	@Override
	public String toString() {
		return "KeyData [type=" + type + ", code=" + code + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyData other = (KeyData) obj;
		if (code != other.code)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
