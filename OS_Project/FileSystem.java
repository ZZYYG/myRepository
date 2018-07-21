package OS_Project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;




public class FileSystem extends JFrame{
	
	private User_Message user;
	private byte bitmap[] = new byte [BLOCKNUM];//λͼ����	
	private int num_cur;
	private File file = new File("E:/java/java test/src/OS_Project/MemeryTable.txt");
	private ArrayList<I_Node> inode_array = new ArrayList<I_Node>(INODENUM);
	private ArrayList<Block> block_array = new ArrayList<Block>(BLOCKNUM);
	String FileSystemName = "��������ļ�ϵͳ";
	
	public static final int BLOCKSIZE = 512;// ���ݿ�Ĵ�С 
	public static final int BLOCKNUM = 512;// ���ݿ�Ŀ���
	public static final int INODESIZE = 32;// i�ڵ�Ĵ�С
	public static final int INODENUM = 32;// i�ڵ����Ŀ
	public static final int FILENUM = 8;// ���ļ������Ŀ
	public static final int FILETYPE[] = {0,1};//�ļ������ͣ�0�����ļ���1����Ŀ¼
	public static final int READGRANT = 1;
	public static final int WRITEGRANT = 2;
	public static final int DELETEGRANT = 3;
	public static final int CDGRANT = 1;
	public static final int CREAKFILEGRANT = 2;
	public static final int RDGRANT = 3;
	protected JTextField jt1;
	protected JTextField jt2;
	protected JDialog jd;
    private static final long serialVersionUID = -5418344602348249043L;  
    private JPanel pup = new JPanel();  
    private JPanel pdown = new JPanel();  
    private JTextField txtCommand = new JTextField(45);  
    private JTextArea txtContent = new JTextArea();  
    private JButton btnExec = new JButton("Execute");  
    private boolean IsCommand = true;
    private  String command;
    protected static boolean flag;
    private boolean initflag = true;
  
    public void format(){
			println("���ڽ��г�ʼ������");
		
			try {
		
				FileOutputStream fo = new FileOutputStream(file,true);
				
				FileInputStream fi = new FileInputStream(file);
				
				ObjectOutputStream oop = new ObjectOutputStream(fo);
				//��ʼ��λͼ��
				byte []b = new byte[BLOCKNUM];
				
				for(int i=0;i<BLOCKNUM;i++){
					b[i] = 1;
				}	
				fo.write(b);
				//��ʼ��inode���
				inode_array = new ArrayList<>();
				for(int i=0;i<INODENUM;i++){
					I_Node inode = new I_Node(i);
					inode_array.add(inode);
				}
				I_Node inode0 = new I_Node(0,"home",user.getUsername(),FILETYPE[1],-1);
				inode_array.set(0, inode0);
				oop.writeObject(inode_array);
				//��ʼ��������
				block_array = new ArrayList<>();
				
				for(int i=1;i<BLOCKNUM;i++){
					Block block = new Block(i,null);
					block_array.add(block);
				}
				oop.writeObject(block_array);			
				
				fi.close();
				fo.close();
				oop.close();
						
					
				
			}catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				println(e.toString());
			}
			println("��ʼ�����");
		
		}
	public void init() throws FileNotFoundException{
	
		try {
			//file = new File("E:/java/java test/src/OS_Project/MemeryTable.txt");
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oip = new ObjectInputStream(fi);;
			//����λͼ��Ϣ
			fi.read(bitmap);
			//����inode��Ϣ
			inode_array = (ArrayList<I_Node>) oip.readObject();
			//����������Ϣ
			block_array = (ArrayList<Block>)oip.readObject();
			
			fi.close();
			oip.close();
			
			num_cur = 0;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void loginUI(){
			
			jd = new JDialog();
			jd.setLocationRelativeTo(null);
			jd.setTitle("�û���¼");
			jd.setLayout(new FlowLayout());
			jd.setBounds(400,400,300,150);
	//		jd.setSize(new Dimension(300,150));
			JLabel jl1 = new JLabel("�û�����");
			JLabel jl2 = new JLabel("��   �룺");
			jt1 = new JTextField();
			jt1.setPreferredSize(new Dimension(180,20));
			jt2 = new JTextField();
			jt2.setPreferredSize(new Dimension(180,20));
			JButton jb = new JButton("��¼");
	//		jb.addActionListener(ml);
			jd.add(jl1);
			jd.add(jt1);
			jd.add(jl2);
			jd.add(jt2);
			jd.add(jb);
			this.setVisible(true);
			jd.setVisible(true);
			jb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getActionCommand().equals("��¼")){
						try {
							boolean flag = false;
							BufferedReader br = new BufferedReader(new FileReader("E:/java/java test/src/OS_Project/user.txt"));
							String message = null;
							while((message=br.readLine())!=null){
								String messages[] = message.split(",");
								//�����û������������ƥ��
								if(messages[1].equals(jt1.getText())
										&&messages[2].equals(jt2.getText())){
									
									flag = true;
									break;
								}
							}
							br.close();
							if(flag){
								user = new User_Message(jt1.getText(), jt2.getText());
								I_Node inode0 = new I_Node(0,"home",user.getUsername(),FILETYPE[1],-1);
								inode_array.set(0, inode0);
								jd.setVisible(false);
								JDialog newjd = new JDialog();
	//							newjd.setSize(new Dimension(200,150));
								newjd.setBounds(450,450,200,150);
								newjd.setLayout(new FlowLayout());
								JLabel jl = new JLabel("��¼�ɹ�\r\n");
								jl.setFont(new Font("Dialog", 1, 30));
								JButton jb = new JButton("ȷ��");
								newjd.add(jl);
								newjd.add(jb);
								newjd.setVisible(true);
								jb.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										// TODO Auto-generated method stub
										newjd.setVisible(false);
										num_cur = 0;
								        txtContent.setText("��ӭ����"+user.getUsername()+"���ļ�����ϵͳ\r\n");
								        help();
								        showcur();
								        if(initflag){
								        	initUI();
								        	initflag = false;
								        }
									}
								});
							}else{
								jd.setVisible(false);
								JDialog newjd = new JDialog();
	//							newjd.setSize(new Dimension(200,150));
								newjd.setLayout(new FlowLayout());
								newjd.setBounds(450,450,200,150);
	//							newjd.setLocationRelativeTo(null);
								JLabel jl = new JLabel("������������µ�¼");
								jl.setFont(new Font("Dialog", 1, 15));
								JButton jb = new JButton("ȷ��");
								newjd.add(jl);
								newjd.add(jb);
								newjd.setVisible(true);
								jb.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										// TODO Auto-generated method stub
										newjd.setVisible(false);
										jt1.setText("");
										jt2.setText("");
										jd.setVisible(true);
									}
								});
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			
		}
	public void loginOutUI(){
			JDialog jd = new JDialog();
			jd.setLayout(new FlowLayout());
	//		jd.setSize(new Dimension(200,150));
	//		jd.setLocationRelativeTo(null);
			jd.setBounds(350, 350, 200, 150);
			JLabel jl = new JLabel("�Ƿ��˳���ǰ�û�\r\n");
			jl.setFont(new Font("Dialog", 1, 20));
			JButton jb1 = new JButton("��");
			JButton jb2 = new JButton("��");
			jd.add(jl);
			jd.add(jb1);
			jd.add(jb2);
			jd.setVisible(true);
			jb1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					jd.setVisible(false);
					loginUI();
				}
			});
			jb2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					jd.setVisible(false);
				}
			});
		}
	public void initUI() {  
        // ָ����ܵĲ��ֹ�����  
        setLayout(new BorderLayout());
        
        // �����ı���,�ı�������  
        txtCommand.setFont(new Font("", Font.BOLD, 13));  
        txtContent.setFont(new Font("", Font.BOLD, 13));  
        // ָ�����Ĳ���  
        pup.setLayout(new BorderLayout());  
        pdown.setLayout(new FlowLayout());  
  
        // ���ı�����ӵ������  
        pup.add(txtContent);  
        // Ϊ�ı�����ӹ�����  
        pup.add(new JScrollPane(txtContent));  
        // ���ı���,��ť��ӵ������  
        pdown.add(txtCommand);  
        pdown.add(btnExec);  
        setBounds(300,300,300,500);
        // �����嵽�����  
        this.add(pup, BorderLayout.CENTER);  
        this.add(pdown, BorderLayout.SOUTH);  
  
        // �����¼�  

        txtContent.setEditable(false);
        // ��Ӱ�ť�¼�  
        btnExec.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                String s;  
                // ��ȡ�ı����е�����  
                command = txtCommand.getText().trim(); 
                txtCommand.setText("");
                txtContent.append(command+"\r\n");
                if(IsCommand){
                	switchCommend(command);
                }
                command = null;
            	
            }  
        });  
  
        // ��Ӽ���Enter�¼�  
        txtCommand.addKeyListener(new KeyListener() {  
  
            public void keyPressed(KeyEvent e) {  
                // �����»س�ʱ  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String s;  
                    // ��ȡ�ı����е�����  
                    String command = txtCommand.getText().trim(); 
                    txtCommand.setText("");
                    txtContent.append(command+"\r\n");
                    if(IsCommand){
                    	switchCommend(command);
                    }
                }  
            }  
  
            public void keyReleased(KeyEvent e) {  
            }  
  
            public void keyTyped(KeyEvent e) {  
            }  
        });  
        this.setTitle("MyDOS");  
        this.setSize(666, 444);  
        this.setVisible(true);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
	public void switchCommend(String commend){//ָ���ת��
		IsCommand = false;
	
		if(commend.equals("ls")){
			ls();
		}else if(commend.startsWith("mkdir")){
			String commends[]=commend.split(" ");
			mkdir(commends[1]);
		}else if(commend.startsWith("cd")){
			String commends[]=commend.split(" ");
			cd(commends[1]);
		}else if(commend.startsWith("rd")){
			String commends[]=commend.split(" ");
			rd(commends[1]);
		}else if(commend.startsWith("vi")){
			String commends[]=commend.split(" ");
			vi(commends[1]);
		}else if(commend.startsWith("del")){
			String commends[]=commend.split(" ");
			delete(commends[1]);
		}else if(commend.equals("showmap")){
			showmap();
		}else if(commend.equals("showfile")){
			showFileMessage();
		}else if(commend.equals("quit")){
			quit();
		}else if(commend.equals("format")){
			format();
		}else if(commend.equals("loginout")){
			loginOutUI();
		}else if(commend.equals("grant")){
			grantUI();
		}else if(commend.equals("clear")){
			clear();
		}else if(commend.equals("help")){
			help();
		}else if(commend.equals("regrant")){
			regrantUI();
		}else if(commend.startsWith("showblock")){
			String commends[]=commend.split(" ");
			showBlock(commends[1]);
		}else{
			println("��Ч����");
		}
		IsCommand = true;
		showcur();
	}
	public void help(){
		String helpmessage[] = {"command: \r\n",
				"help   ---  ��ʾ������Ϣ \r\n",
				"cd     ---  ����ָ��Ŀ¼ \r\n",
				"mkdir  ---  �����µ�Ŀ¼   \r\n",
				"rd     ---  ɾ��ĳ��Ŀ¼�����ڲ������ļ���Ŀ¼\r\n",
				"vi     ---  �������ָ���ļ�   \r\n",
				"del    ---  ɾ��ָ���ļ���Ŀ¼ \r\n",
				"loginout--  �˳���¼\r\n",
				"clear  ---  �����Ļ\r\n",
				"grant  ---  ��ָ���ļ���Ŀ¼������Ȩr\n",
				"showmap---  �鿴����ʹ�����\r\n",
				"showfile--  �鿴��ǰ�ļ���Ŀ¼�����\r\n",
				"format ---  ��ʼ���ļ�ϵͳ \r\n",
				"save   ---  ���浱ǰ�޸�\r\n",
				"quit   ---  �˳��ļ�ϵͳ\r\n"};
		for(int i=0;i<helpmessage.length;i++){
			print(helpmessage[i]);
		}

		
	}
	
	public void ls(){//�г���ǰĿ¼�µ��ļ���Ŀ¼
		boolean flag = true;
		for(int i=0;i<inode_array.size();i++){
			I_Node inode = inode_array.get(i);	
			if(inode.getiParent()==num_cur){
				if(inode.getType()==FILETYPE[1])
					print(".");
				print(inode.getFile_name()+" ");
				flag = false;
			}
		}
		if(flag)
			println("��ǰĿ¼���ļ�");
		else
			println();
	}
	public void clear(){
		txtContent.setText("");
	}
	/**
	 * �˳������������Ĺ��Ĳ��������ļ���
	 */
	public void quit(){
		JDialog jd = new JDialog();
		jd.setLayout(new FlowLayout());
		jd.setSize(new Dimension(300,150));
		jd.setLocationRelativeTo(null);
		JLabel jl = new JLabel("�Ƿ��˳������ļ�ϵͳ���޸�\r\n");
		jl.setFont(new Font("Dialog", 1, 20));
		JButton jb1 = new JButton("��");
		JButton jb2 = new JButton("��");
		jd.add(jl);
		jd.add(jb1);
		jd.add(jb2);
		jd.setVisible(true);
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jd.setVisible(false);
				save();
				System.exit(0);
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jd.setVisible(false);
			}
		});
		
		

	}
	/**
	 * �������ļ�
	 * ����i�ڵ��������пսڵ�����з���
	 */
	public void vi(String filename){//�����ļ�ֱ�Ӵ�ԭ�ļ�����Ҫ�´���
		int i,j;
		
		boolean flag = false;
		boolean grantflag = false;
		boolean createflag = true;
		for(i=0;i<inode_array.size();i++){
			I_Node inode = inode_array.get(i);
			if(inode.getFile_name().equals(filename)&&inode.getType()==FILETYPE[0]&&inode.getiParent()==num_cur){
				createflag = false;
				try {
					if(checkgrant(inode, READGRANT)){
						read(inode.getiNum());
						grantflag = false;
						break;//����������һ�䣬��Ȼ�ִ������ļ�ȥ��
					}else{
						showDialog("δ��ȡ��Ȩ");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
			}
				
		if(createflag){
			println("�������ļ�");
			createfile(filename);
		}
	}


	/**
	 * ��ȡ�ļ�
	 * �ļ��򿪱����ҵ����򿪽ڵ���Ϣ�����ݴ��̿���Ϣ�ҵ����̿�λ�ö�ȡ
	 * @throws IOException 
	 */
	public void read(int inum) throws IOException{
		I_Node inode = inode_array.get(inum);
		String content = null;
		boolean flag = false;
		for(int i=0;i<inode.getAddress().size();i++){
			int address = inode.getAddress().get(i);
			for(int j=0;j<block_array.size();j++){
				Block block = block_array.get(j);
				
				if(block.getNum()==address){
					if(block.getMemery()!=null){
						content = new String(block.getMemery());
						flag = true;
						break;
					}
				}
			}
		}
		if(flag){
			readUI(inode.getFile_name(),content,inode);
		}else{
			readUI(inode.getFile_name(),"",inode);
		}
	}
	public void readUI(String filename,String content, I_Node inode) {
		// TODO Auto-generated method stub
			JDialog jd = new JDialog();
			jd.setLocationRelativeTo(this);
	//		jd.setSize(new Dimension(500,300));
			jd.setBounds(300,300,500,300);
			jd.setTitle(filename);
			JTextArea jt = new JTextArea();
			jt.setEditable(false);
			jt.setSize(new Dimension(500,250));
			JButton jb = new JButton("�༭");
			jb.setSize(new Dimension(100,50));
			jd.add(jt,BorderLayout.CENTER);
			jd.add(jb,BorderLayout.SOUTH);
			jt.setText(content);
			jd.setVisible(true);
			jb.addActionListener(new ActionListener() {
				int length;
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getActionCommand().equals("�༭")){
						if(checkgrant(inode, WRITEGRANT)){
							jt.setEditable(true);
							jb.setText("����");
						}else{
							showDialog("δ��ȡ��Ȩ");
							
						}
					}
					if(e.getActionCommand().equals("����")){
						jt.setEditable(false);
						jb.setText("�༭");
						ArrayList<Integer> address_list = inode.getAddress();
						for(int index=0;index<address_list.size();index++){
							int address = address_list.get(index);
							bitmap[address] = 1;
							Block block = new Block(address);
							block_array.set(address, block);
						}
							String str = jt.getText();
							
							int Blocknum = str.toString().getBytes().length/BLOCKSIZE + 1;
							System.out.println(Blocknum+","+address_list.size());
							byte b[] = new byte[inode.toString().getBytes().length];
							b = str.toString().getBytes();
							
							length = b.length;
							inode.setLength(length);
							byte newb[][];
							if(Blocknum==1){
								newb = new byte[1][b.length];
								System.arraycopy(b, 0,newb[0],0,b.length);
							}else{
								int i;
								newb = new byte [Blocknum][BLOCKSIZE];
								newb[Blocknum-1] = new byte[b.length%BLOCKSIZE];
								for(i=0;i<Blocknum-1;i++){
									System.arraycopy(b, i*BLOCKSIZE,newb[i],0,BLOCKSIZE);
								}
								System.arraycopy(b, i*BLOCKSIZE,newb[i],0,b.length%BLOCKSIZE);
							}
							if(Blocknum>address_list.size()){
								
								int j;
								for(j=0;j<address_list.size();j++){
									int add = address_list.get(j);
									bitmap[add] = 0;
									block_array.get(add).setMemery(newb[j]);		
									inode.getAddress().add(add);
									
								}
							
							for(int blockindex=0;blockindex<BLOCKNUM;blockindex++)
								if(bitmap[blockindex]==1){
									block_array.get(blockindex).setMemery(newb[j++]);
									inode.getAddress().add(blockindex);
									bitmap[blockindex] = 0;		
									if(j==Blocknum)
										break;
									}				
							}else{
								for(int j=0;j<Blocknum;j++){
									int ad = address_list.get(j);
									bitmap[ad] = 0;
									block_array.get(ad).setMemery(newb[j]);		
									inode.getAddress().add(ad);
								}
							}
							
						}
					}
				
			});
		}
	public void createfile(String filename){
		int i;
		I_Node newinode = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(i=1;i<inode_array.size();i++){
			I_Node inode = inode_array.get(i);
			
			if(inode.IsAvailable()){
				inode = new I_Node(i, filename, user.getUsername(),FILETYPE[0], num_cur);
				
				inode.setAvailable(false);
				inode_array.set(i, inode);
				newinode = inode;
				break;
			}
		}
		if(i==inode_array.size()){
			println("��������");
		}else{		
			readUI(newinode.getFile_name(), "", newinode);
		}
	}
	/**
	 * ɾ���ļ�
	 * ����i�ڵ������ҵ���ɾ���ڵ㣬ɾ��i�ڵ���Ϣ���ͷŴ��̿飬λʾͼ��0
	 */
	public void delete(String filename){
		ArrayList< Integer> address_list = new ArrayList<Integer>();
		for(int i=0;i<inode_array.size();i++){
			I_Node inode = inode_array.get(i);
			if(inode.getFile_name().equals(filename)&&inode.getiParent()==num_cur&&inode.getType()==FILETYPE[0]){
				if(checkgrant(inode, DELETEGRANT)){
					I_Node newinode = new I_Node(i);
					newinode.setAvailable(true);
					inode_array.set(i, newinode);
					deleate_block(address_list);
					break;
				}else{
					showDialog("δ��ȡ��Ȩ");
				}
			}			
		}
	}
	public void deleate_block(ArrayList<Integer> address_list) {
		// TODO Auto-generated method stub
		for(int i=0;i<address_list.size();i++){
			bitmap[address_list.get(i)] = 1;
			Block newblock = new Block(address_list.get(i));
			block_array.set(address_list.get(i), newblock);
		}
	}


	
	public void cd(String dirname){//����Ŀ¼q
		boolean flag = false;
		if(dirname.equals("..")){
			flag = true;
			num_cur = inode_array.get(num_cur).getiParent();
		}else{
			int i,j = 0;
			for(i=0;i<inode_array.size();i++){
				I_Node inode = inode_array.get(i);
				if(inode.getFile_name().equals(dirname)&&inode.getType()==FILETYPE[1]&&inode.getiParent()==num_cur){
					flag = true;
					if(checkgrant(inode,CDGRANT)){	
						flag = true;
						num_cur = inode.getiNum();
						break;
					}else{
						showDialog("δ��ȡ��Ȩ");
						break;
					}
				}
			}
				
		}
		if(!flag)
			println("��ǰĿ¼�޸��ļ���");
	}
	public void mkdir(String dirname){//����Ŀ¼
		for(int j=1;j<inode_array.size();j++){
			I_Node inode = inode_array.get(j);
			if(inode.getFile_name().equals(dirname)&&inode.getType()==FILETYPE[1]&&inode.getiParent()==num_cur){
				println("�Ѵ��ڸ��ļ���");
				return;
			}
		}
		if(checkgrant(inode_array.get(num_cur),CREAKFILEGRANT)){
			int i;
			for(i=1;i<inode_array.size();i++){
				I_Node inode = inode_array.get(i);
				if(inode.IsAvailable()){
					I_Node newinode = new I_Node(i, dirname, user.getUsername(),  FILETYPE[1],num_cur);//ע�⹹�캯����˳��
					newinode.setAvailable(false);
					inode_array.set(i, newinode);
					break;
				}
			}
			if(i==inode_array.size()){
				println("��������");
			}
		}else{
			showDialog("δ��ȡ��Ȩ");
		}
	}
	public void rd(String dirname){//ɾ��Ŀ¼,�ݹ�ɾ��������Ŀ¼���ļ�
		boolean Isflag = false;
		for(int i=0;i<inode_array.size();i++){//ע��ɾ���ķ�ʽ�����´�������
			I_Node inode = inode_array.get(i);
			if(inode.getType()==FILETYPE[1]&&inode.getiParent()==num_cur&&inode.getFile_name().equals(dirname)){
				Isflag = true;
				if(checkgrant(inode, RDGRANT)){
					I_Node newinode = new I_Node(i);
					newinode.setAvailable(true);
					inode_array.set(i, newinode);
					
					for(int j=0;j<inode_array.size();j++){
						I_Node soninode = inode_array.get(j);
						if(soninode.getiParent()==i){
							if(soninode.getType()==FILETYPE[0]){
								delete(soninode.getFile_name());
							}else{
								rd(soninode.getFile_name());
							}
						}
					}
				}else{
					showDialog("δ��ȡ��Ȩ");
				}
			}
		}
		if(!Isflag){
			println("���ļ��в�����");
		}
		
	}
	public void grantUI(){
		jd = new JDialog();
		jd.setTitle("�û���Ȩ");
		jd.setLayout(new FlowLayout());
		jd.setLocationRelativeTo(this);
		jd.setBounds(350, 350, 300, 250);
		JLabel jl1 = new JLabel("ѡ�����:");
		JLabel jl2 = new JLabel("ѡ���ļ�/�ļ���:");
		JLabel jl3 = new JLabel("�������Ȩ��:1-���� 2-�޸� 3-ɾ��");
		jl3.setSize(new Dimension(250,40));
		JTextField jt1 = new JTextField();
		jt1.setPreferredSize(new Dimension(250,20));
		JTextField jt2 = new JTextField();
		jt2.setPreferredSize(new Dimension(250,20));
		JTextField jt3 = new JTextField();
		jt3.setPreferredSize(new Dimension(250,20));
		JButton jb = new JButton("ȷ����Ȩ");
		jd.add(jl1);
		jd.add(jt1);
		jd.add(jl2);
		jd.add(jt2);
		jd.add(jl3);
		jd.add(jt3);
		jd.add(jb);
		this.setVisible(true);
		jd.setVisible(true);
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean flag = false;
				for(int i=0;i<inode_array.size();i++){
					I_Node inode = inode_array.get(i);
					if(inode.getiParent()==num_cur&&inode.getFile_name().equals(jt2.getText())){
						if(inode.getGrantuser().get(user.getUsername())!=3){
							showDialog("���߱����ʸ�");
							return;
						}
						flag = true;
						inode.getGrantuser().put(jt1.getText(),Integer.parseInt(jt3.getText()));
						break;
					}
				}
				if(flag){
					jd.setVisible(false);
					showDialog("��Ȩ�ɹ�");
				}
			}
			
		});
		
	}
	public void showBlock(String filename){
		for(int j=0;j<inode_array.size();j++){
			I_Node inode = inode_array.get(j);
			if(inode.getFile_name().equals(filename)){
				println("�ļ�����:"+inode.getLength()+"�ֽ�");
				for(int i =0;i<inode.getAddress().size();i++){
					int address = inode.getAddress().get(i);
					
					print(address+" ");
				}
				println("");
				break;
			}
		}
	}
	public void regrantUI(){
		jd = new JDialog();
		jd.setTitle("�ջ���Ȩ");
		jd.setLayout(new FlowLayout());
		jd.setLocationRelativeTo(this);
		jd.setBounds(350, 350, 300, 250);
		JLabel jl1 = new JLabel("ѡ�����:");
		JLabel jl2 = new JLabel("ѡ���ļ�/�ļ���:");
		JLabel jl3 = new JLabel("�ջط���Ȩ��:1-���� 2-�޸� 3-ɾ��");
		jl3.setSize(new Dimension(250,40));
		JTextField jt1 = new JTextField();
		jt1.setPreferredSize(new Dimension(250,20));
		JTextField jt2 = new JTextField();
		jt2.setPreferredSize(new Dimension(250,20));
		JTextField jt3 = new JTextField();
		jt3.setPreferredSize(new Dimension(250,20));
		JButton jb = new JButton("�ջ���Ȩ");
		jd.add(jl1);
		jd.add(jt1);
		jd.add(jl2);
		jd.add(jt2);
		jd.add(jl3);
		jd.add(jt3);
		jd.add(jb);
		jd.setVisible(true);
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean flag = false;
				for(int i=0;i<inode_array.size();i++){
					I_Node inode = inode_array.get(i);
					if(inode.getiParent()==num_cur&&inode.getFile_name().equals(jt2.getText())){
						if(inode.getGrantuser().get(user.getUsername())!=3){
							showDialog("���߱����ʸ�");
							return;
						}
						
						flag = true;
						int grant = (inode.getGrantuser().containsKey(jt1.getText()))?inode.getGrantuser().get(jt1.getText()):0;
						if(grant>=Integer.parseInt(jt3.getText())){	
							if(Integer.parseInt(jt3.getText())==1){
								inode.getGrantuser().remove(jt1.getText());
							}else{
								inode.getGrantuser().put(jt1.getText(), Integer.parseInt(jt3.getText())-1);
							}
						}else{
							jd.setVisible(false);
							showDialog("����û�û�и�Ȩ��");
							return;
						}
						break;
					}
				}
				if(flag){
					jd.setVisible(false);
					showDialog("�ɹ��ջ�Ȩ��");
				}else{
					showDialog("�����ڸ��ļ�");
				}
			}
			
		});
		
	}
	
	public void showcur(){
		String str = "";
		int num = num_cur;
		while(num!=-1){
			str = inode_array.get(num).getFile_name()+"/"+str;
			num = inode_array.get(num).getiParent();
		}
		print(str);
	}
	
	public void showmap(){
		for(int i=0;i<BLOCKSIZE;i++){
			print(bitmap[i]+"");
			if((i+1)%32==0)
				println();
		}
	}
	public void showFileMessage(){
		println("ID   �ļ���      ����      �����û�");
		String filetype;
		for(int i=0;i<INODESIZE;i++){
			I_Node inode = inode_array.get(i);
			if(!inode.IsAvailable()){
				print(i+"");
				if(inode.getType()==FILETYPE[1]){
					filetype = "�ļ���";
				}else{
					filetype = "�ļ�";
				}
			
				print("  "+inode.getFile_name()+"  "+filetype+"  ");
				Iterator<Map.Entry<String, Integer>> it = inode.getGrantuser().entrySet().iterator();
				while(it.hasNext()){
					Entry<String, Integer> entry = it.next();
					print(entry.getKey());
					if(entry.getValue()==1){
						print("(�ɷ���)");
					}else if(entry.getValue()==2){
						print("(�ɷ��ʡ��޸�)");
					}else if(entry.getValue()==3){
						print("(�ɷ��ʡ��޸ġ�ɾ��)");
					}
				}
				println();
			}	
			
		}
	}

	public void showDialog(String str){
		JDialog newjd = new JDialog();
		newjd.setBounds(400,400,210,150);	
		newjd.setLayout(new FlowLayout());
		newjd.setLocationRelativeTo(null);
		JLabel jl = new JLabel(str);
		jl.setFont(new Font("Dialog", 1, 30));
		JButton jb = new JButton("ȷ��");
		newjd.add(jl);
		newjd.add(jb);
		newjd.setVisible(true);
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newjd.setVisible(false);
			}
		});
	}
	public void save(){
					
			try {
				file = new File("E:/java/java test/src/OS_Project/MemeryTable.txt");
				
				file.delete();
				
				File newfile = new File("E:/java/java test/src/OS_Project/MemeryTable.txt");	
				
				FileOutputStream fo;
				
					fo = new FileOutputStream(newfile,true);
					ObjectOutputStream oop = new ObjectOutputStream(fo);
					
					fo.write(bitmap);
				oop.writeObject(inode_array);
				oop.writeObject(block_array);
				
				fo.close();
				oop.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	public void print(String str){
		txtContent.append(str);
	}
	public void println(String str){
		txtContent.append(str+"\r\n");
	}
	public void println(){
		txtContent.append("\r\n");
	}

	public boolean checkgrant(I_Node inode,int rank){
		HashMap<String, Integer> usergrant = inode.getGrantuser();
		int filerank;
		if(!usergrant.containsKey(user.getUsername())){
			filerank = 0;
		}else{
			filerank = usergrant.get(user.getUsername());
		}
		System.out.println(user.getUsername());
		Iterator<Map.Entry<String, Integer>> it = inode.getGrantuser().entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Integer> entry = it.next();
			System.out.println(entry.getKey()+","+entry.getValue());
		}
		System.out.println(filerank+","+rank);
		if(rank>filerank){
			return false;
		}else{
			return true;
		}
	}
	public static void main(String[] args) throws IOException {
		FileSystem mf = new FileSystem();
//		mf.format();
		mf.init();
		mf.loginUI();

	}
}
