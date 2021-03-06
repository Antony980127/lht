package com.bth.lht.rest.socket;

import com.bth.lht.dto.ChatMessage;
import com.bth.lht.util.ServerEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: lht
 * @description: 即时通讯类
 * @author: Antony
 * @create: 2019-03-13 09:37
 **/
@ServerEndpoint(value = "/websocket/{openid}",encoders = ServerEncoder.class)
@Component
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;


    //消息对象
    private ChatMessage chatMessage = null;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("openid") String openid) {
        chatMessage = new ChatMessage();
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！加入的人为"+openid+",当前在线人数为" + getOnlineCount());
        try {
            sendMessage("XXXXX加入群聊",openid);
        } catch (IOException e) {
            System.out.println("IO异常");
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("openid") String openid) {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("openid") String openid) {
        System.out.println("来自"+openid+"的消息:" + message);

        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message,openid);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     * /
     * */
     @OnError
     public void onError(Session session, Throwable error,@PathParam("openid") String openid) {
     System.out.println("发生错误");
     error.printStackTrace();
     }


     public void sendMessage(String message,@PathParam("openid") String openid) throws IOException, EncodeException {

         chatMessage.setOpenid(openid);
         chatMessage.setMessage(message);
     this.session.getBasicRemote().sendObject(chatMessage);
     //this.session.getAsyncRemote().sendText(message);
     }


     /**
      * 群发自定义消息
      * */
    public static void sendInfo(String message,@PathParam("openid") String openid) throws IOException {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message,openid);
            } catch (IOException e) {
                continue;
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
