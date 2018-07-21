package OS_Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class I_Node implements Serializable{
	private int iNum;  //节点号
	private String file_name = "\0";
	private int type = 0;    //文件类型,目录还是文件
	private HashMap<String, Integer> grantuser = new HashMap<String,Integer>();//权限，1--可读，2--可编辑
	private int iParent = -1;  //父目录的i节点号
	private int length;   //文件长度
	private boolean IsAvailable = true;
	private ArrayList<Integer> address_list = new ArrayList<Integer>();
	
	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public int getiParent() {
		return iParent;
	}

	public void setiParent(int iParent) {
		this.iParent = iParent;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean IsAvailable() {
		return IsAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.IsAvailable = isAvailable;
	}

	public ArrayList<Integer> getAddress() {
		return address_list;
	}

	public void setAddress(ArrayList<Integer> address_list) {
		this.address_list = address_list;
	}

	public int getiNum() {
		return iNum;
	}

	public void setiNum(int iNum) {
		this.iNum = iNum;
	}

	public I_Node(int iNum) {
		super();
		this.iNum = iNum;
	}

	public HashMap<String, Integer> getGrantuser() {
		return grantuser;
	}

	public void setGrantuser(HashMap<String, Integer> grantuser) {
		this.grantuser = grantuser;
	}

	public I_Node(int iNum, String file_name, String username, int type,int iParent) {
		super();
		this.iNum = iNum;
		this.file_name = file_name;
		this.iParent = iParent;
		this.type = type;
		this.grantuser.put(username, 3);
	}
	
}
