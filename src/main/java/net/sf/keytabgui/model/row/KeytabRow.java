package net.sf.keytabgui.model.row;

import net.sf.keytabgui.util.ToString;

public class KeytabRow {
	
	private int size;
	private short numComponents;
	private String realm;
	private String[] components;
	private int timestamp;
	private int vno8;
	private int keyblockType;
	private byte[] keyblock;
	private Integer vno;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public short getNumComponents() {
		return numComponents;
	}
	public void setNumComponents(short numComponents) {
		this.numComponents = numComponents;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String[] getComponents() {
		return components;
	}
	public void setComponents(String[] components) {
		this.components = components;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public int getVno8() {
		return vno8;
	}
	public void setVno8(int vno8) {
		this.vno8 = vno8;
	}
	public int getKeyblockType() {
		return keyblockType;
	}
	public void setKeyblockType(int keyblockType) {
		this.keyblockType = keyblockType;
	}
	public byte[] getKeyblock() {
		return keyblock;
	}
	public void setKeyblock(byte[] keyblock) {
		this.keyblock = keyblock;
	}
	public Integer getVno() {
		return vno;
	}
	public void setVno(Integer vno) {
		this.vno = vno;
	}
	
	
	public int countSize() {
		int sum = 120; // size+numComponents+nameType+timestamp+vno8
		sum += realm.length()*8;
		for (String c: components){
			sum += c.length()*8;
		}
		if (vno!=null)
			sum += 4;
		return sum;
	}
	
	public String toString(){
		return ToString.toString(this);
	}

}
