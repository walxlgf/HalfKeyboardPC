/**
 * 
 */
package com.single.handed.vo;

/**
 * @author Thinkpad
 *
 */
public class KeyValue {
	private String name;
	private int code;
    private boolean isShifted;

    public boolean isShifted() {
        return isShifted;
    }

    public void setShifted(boolean shifted) {
        isShifted = shifted;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeyValue [name=");
		builder.append(name);
		builder.append(", code=");
		builder.append(code);
		builder.append("]");
		return builder.toString();
	}
	
	

}
