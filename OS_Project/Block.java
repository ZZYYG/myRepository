package OS_Project;

import java.io.Serializable;

public class Block implements Serializable{
	public static final int BLOCKSIZE = 512;
	private int num;
	private byte []memery = new byte[512];
	public Block(int num,byte[] memery) {
		super();
		this.num = num;
		this.memery = memery;
	}
	public Block(int num) {
		super();
		this.num = num;
	}
	public void setMemery(byte[] memery) {
		this.memery = memery;
	}
	
	public byte[] getMemery() {
		return memery;
	}
	public int getNum() {
		return num;
	}
	
	
}
