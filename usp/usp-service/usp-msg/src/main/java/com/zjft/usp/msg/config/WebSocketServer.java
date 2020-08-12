package com.zjft.usp.msg.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CK
 * @date 2019-11-13 15:27
 */
@ServerEndpoint(value = "/websocket/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    private static Map<Session, Long> sessionUserIdMap = new ConcurrentHashMap<>();
    private static Map<Long, Session> userIdSessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        sessionUserIdMap.put(session, userId);
        userIdSessionMap.put(userId, session);
        log.info(userId + ":已连接");
    }

    @OnClose
    public void onClose(Session session) {
        Long userId = sessionUserIdMap.remove(session);
        if (userId != null) {
            userIdSessionMap.remove(userId);
        }
        log.info(session + ":关闭连接");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        sendMessageToAll("给所有人发信息");
        log.info("WebSocketServer:onMessage:" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendMessageToOne(Long userId, String message) {
        Session session = userIdSessionMap.get(userId);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessageToAll(String message) {
        Iterator it = sessionUserIdMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Session key = (Session) entry.getKey();
            key.getAsyncRemote().sendText(message);
        }
    }
}
