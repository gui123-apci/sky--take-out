package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 服务端
 * 商家端浏览器通过 ws://ip:port/ws/{clientId} 建立连接
 */
@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    /**
     * 保存所有连接的会话，key 是客户端标识 sid，value 是会话对象
     * 用 static 保证多个实例共享同一份连接列表
     */
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 连接建立成功时调用
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("客户端连接成功，sid：{}", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端发来的消息时调用（本项目主要靠服务端主动推送，这里仅打印）
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到客户端({})发来的消息：{}", sid, message);
    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("客户端断开连接，sid：{}", sid);
        sessionMap.remove(sid);
    }

    /**
     * 群发消息：把消息推送给所有连接的商家端
     *
     * @param message json 字符串
     */
    public void sendAll(String message) {
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            Session session = entry.getValue();
            log.info("给客户端({})推送消息：{}", entry.getKey(), message);
            // getAsyncRemote 异步发送，避免某个客户端卡住影响其他客户端
            session.getAsyncRemote().sendText(message);
        }
    }
}