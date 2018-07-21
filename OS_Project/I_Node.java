package OS_Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class I_Node implements Serializable{
	private int iNum;  //�ڵ��
	private String file_name = "\0";
	private int type = 0;    //�ļ�����,Ŀ¼�����ļ�
	private HashMap<String, Integer> grantuser = new HashMap<String,Integer>();//Ȩ�ޣ�1--�ɶ���2--�ɱ༭
	private int iParent = -1;  //��Ŀ¼��i�ڵ��
	private int length;   //�ļ�����
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
