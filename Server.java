package 第四次;
package ne;
import java.awt.*; 
import java.awt.event.*; 
import java.util.*; 
import java.io.*; 
import java.net.*; 
import javax.swing.*; 
public class Server{ 
	public static void main(String[] args){ 
		MyFrame serve = new MyFrame(); 
		serve.setTitle("服务器");
		serve.setVisible(true); 
		serve.setResizable(false); //设置此窗体是否可由用户调整大小
		serve.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置默认关闭操作
	}
} 
class MyFrame extends JFrame{ 
	JTextField port; 
	JButton start; 
	JTextArea content; 
	JTextField cin; 
	JButton say; 
	Socket socket; 
	MyFrame(){ 
		init(); 
		StartListen sListen = new StartListen(); 
		SayListen stListen = new SayListen(); 
		start.addActionListener(sListen); //为按钮 start 注册一个监听器
		say.addActionListener(stListen); //为按钮 say注册一个监听器
		} 
	void init(){ 
		setLayout(new FlowLayout()); //设置窗体为流式布局
		setSize(400,400); 
		setLocation(400,100); 
		add(new JLabel("Port:")); 
		port = new JTextField("8888",25); 
		add(port); 
		start = new JButton("Start"); 
		add(start); 
		content = new JTextArea(15,35); 
		JScrollPane scroll = new JScrollPane(content); //设置滚动条
		add(scroll); 
		add(new JLabel("Say:")); 
		cin = new JTextField("Hello！",26); 
		add(cin); 
		say = new JButton("Say"); 
		add(say); 
		} 
	class StartListen implements ActionListener{ 
		public void actionPerformed(ActionEvent e){ 
			start.setEnabled(false); 
			try { 
				ServerSocket s = new ServerSocket(Integer.parseInt(port.getText())); // 创建一个服务器套接字对象 s，形参为从 port 文本框中读取的整型（端口号）
				socket = s.accept(); 
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);// 创建一个打印输出流对象，其形参为从套接字 socket 对象中获取的输出流
				out.println("Connected"); 
				content.append("Client connected"+"\n"); //对两个字符串进行拼接
				ServerThread st = new ServerThread(); //创建一个 ServerThread 对象st，并调用其构造方法
				st.start(); //启动一个线程，并调用 run（）方法
				} catch (Exception ex) {} 
			} 
		} 
	class SayListen implements ActionListener{ 
		String str; 
		public void actionPerformed(ActionEvent e){ 
			try {PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);// 创建一个打印输出流，形参为从套接字socket 中获取的输出流
			str=cin.getText(); 
			if(!str.isEmpty()){ 
				out.println(new Date()+"\n"+str); //打印输出日期和发送的消息（ str）
				content.append(new Date()+" \n me:"+str+"\n"); 
				out.flush(); //清空缓存区
				}
			cin.setText(""); 
			} catch (Exception ex) {} 
			} 
		} 
	class ServerThread extends Thread{ 
		public void run(){ 
			try { 
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //创建一个缓冲输出流，其形参为从套接字 socket 中获取的输入流
				String str; 
				while(true){ 
					str = in.readLine(); //按行读取
					content.append( str+"\n"); 
					} 
				} catch (Exception ex) {} 
			} 
		} 
}
