
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by hedingwei on 6/2/16.
 */
public class a {

    static Connection conn2;
    public static void main(String[] args) throws IOException, XMPPException, InterruptedException {

//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    try {
//                        Thread.sleep(3000);
//                        if(conn2==null){
//                            System.out.printf("\f状态:%-50s  \r","未连接");
//                        }else{
//                            System.out.printf("\f状态:%-50s  \r",conn2.isAuthenticated()?"在线":"离线");
//                        }
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();

        input();







    }


    public static void start(String username, String password) throws Throwable{
        //        Connection conn1 = new XMPPConnection("jabber.org");
//        conn1.connect();

        // Create a connection to the jabber.org server on a specific port.
        ConnectionConfiguration config = new ConnectionConfiguration("we.ambimmort.com", 5222);
        conn2 = new XMPPConnection(config);

        conn2.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
//                System.out.println(packet.getClass()+"\t "+packet.toXML());
//                System.out.println(packet.toXML());
                Message message = (Message)packet;
                PacketExtension chatstate = message.getExtension("http://jabber.org/protocol/chatstates");
                if(chatstate!=null){
                    String s = message.getExtension("http://jabber.org/protocol/chatstates").getElementName();
                    if("composing".equals(s)){
                        System.out.printf("\f状态:%-50s  \r","对方正在输入...");
                    }else if("active".equals(s)){
                        System.out.printf("\f状态:%-50s  \r","对方正在输入...");
                    }else if("inactive".equals(s)){
//                    System.out.printf("\f状态:%-50s  \r","...）");
                    }else if("gone".equals(s)){
                        System.out.printf("\f状态:%-50s  \r","与服务端会话完毕...");
                    }
                }



                PacketExtension event = message.getExtension("jabber:x:event");
                if(event!=null){

                    if(message.getBody().startsWith("add")){
                        String d[] = message.getBody().split(" ");
                        double n1 = Double.parseDouble(d[1]);
                        double n2 = Double.parseDouble(d[2]);


                        Message newMessage = new Message();
                        newMessage.setBody("收到,计算结果:"+(n1+n2));
                        newMessage.setType(Message.Type.chat);
                        newMessage.setFrom(message.getTo());
                        newMessage.setTo(message.getFrom());
                        newMessage.setThread(message.getThread());
                        message.setProperty("favoriteColor", "yellow");
                        conn2.sendPacket(newMessage);

                    }else{
                        System.out.println("Server: "+message.getBody());
//                        System.out.println(message.toXML());
                        Message newMessage = new Message();
                        newMessage.setType(Message.Type.chat);
                        newMessage.setFrom(message.getTo());
                        newMessage.setTo(message.getFrom());
                        newMessage.setBody("收到");
                        newMessage.setThread(message.getThread());
                        message.setProperty("favoriteColor", "red");
                        conn2.sendPacket(newMessage);
                    }
                }
//                System.out.println(message.getExtension("http://jabber.org/protocol/chatstates").getElementName());

            }
        },new PacketTypeFilter(Message.class));
        conn2.connect();

        conn2.login(username,password);

        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus("OnLine");

        conn2.sendPacket(presence);
    }

    public static void input(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            String line = scanner.nextLine();
            if(line.equals("exit")){
                System.exit(-1);
            }else{
                if(line.startsWith("login")){
                    System.out.printf("\f状态:%-50s  \r","正在登陆...");
                    String[] d= line.split(" ");
                    if(d.length!=3){

                    }else{
                        final String username = d[1];
                        final String password = d[2];
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        start(username,password);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            }).start();

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            System.exit(-1);
                        }
                    }



                }else if(line.startsWith("state")){
                    String[] d = line.split(" ");
                    if(d.length==2){
                        String state = d[1];
                        Presence presence = new Presence(Presence.Type.available);
                        presence.setStatus(state);
                        conn2.sendPacket(presence);
                    }
                }
            }
        }
    }
}
