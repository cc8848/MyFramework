package com.sys.manager;

import com.sys.entity.Client;
import com.sys.util.ContextHolderUtil;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线用户管理
 * @author zzl
 * Date:2014-08-30
 */
public class ClientManager {
    private static ClientManager clientManager=new ClientManager();
    private Map<String,Client> clientMap=new HashMap<>(0);
    private ClientManager() {
    }
    //获取实例
    public static ClientManager getInstance(){
        return clientManager;
    }
    //获取在线用户信息
    public  Client getClient(String sessionId){
        return clientMap.get(sessionId);
    }
    //获取当前在线用户信息
    public Client getCurrentClient(){
        HttpSession session= ContextHolderUtil.getSession();
        if(session==null)
            return null;
        return clientManager.getClient(session.getId());
    }
    //添加在线用户
    public void addClient(String sessionId, Client client){
        clientMap.put(sessionId,client);
    }
    //获取所有在线用户
    public Collection<Client> getAllClient(){
        return clientMap.values();
    }
    //移出用户
    public void removeClient(String sessionId){
        clientMap.remove(sessionId);
    }
}
