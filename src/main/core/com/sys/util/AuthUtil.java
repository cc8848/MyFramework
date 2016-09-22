package com.sys.util;

import com.sys.entity.Client;
import com.sys.entity.MenuButtonEntity;
import com.sys.entity.SysMenuEntity;
import com.sys.manager.ClientManager;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 权限工具类
 *
 * @author zzl
 *         Date:2014-08-30
 */
public class AuthUtil {
    /**
     * 判断是否有url权限
     *
     * @param url
     * @return
     */
    public static boolean hasUrlAuth(String url) {
        if (url == null) return true;
        Client client = ClientManager.getInstance().getCurrentClient();
        if (client == null)
            return false;
        Set<String> urlSet = client.getMenuEntityMap().keySet();
        if (urlSet == null)
            return false;
        if (urlSet != null) {
            for (String str : urlSet) {
                if (StringUtils.isBlank(str) && StringUtils.isBlank(url))
                    continue;
                if (StringUtils.isBlank(str)) continue;
                if (str.startsWith(url))
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断是否有按钮权限
     *
     * @param url
     * @param buttonCode
     * @return
     */
    public static boolean hasButtonAuth(String url, String buttonCode) {
        boolean success = false;
        if (!hasUrlAuth(url)) return false;
        if (StringUtils.isBlank(buttonCode))
            return true;
        SysMenuEntity menuEntity = ClientManager.getInstance().getCurrentClient().getMenuEntityMap().get(url);
        if (menuEntity == null) return true;
        List<MenuButtonEntity> buttonEntities = menuEntity.getButtonEntities();
        if (buttonEntities == null || StringUtils.isBlank(buttonCode)) return false;
        for (MenuButtonEntity entity : buttonEntities) {
            if (entity.getButtonCode() != null && entity.getButtonCode().equals(buttonCode)) {
                if (entity.isHasPermission())
                    success = true;
            }
        }
        return success;
    }

    /**
     * 判断此按钮是否需要权限
     *
     * @param url
     * @param buttonCode
     * @return
     */
    public static boolean needAuth(String url, String buttonCode) {
        boolean needAuth = false;
        if (!hasUrlAuth(url)) return true;
        SysMenuEntity menuEntity = ClientManager.getInstance().getCurrentClient().getMenuEntityMap().get(url);
        if (menuEntity == null) return false;
        List<MenuButtonEntity> buttonEntities = menuEntity.getButtonEntities();
        if (buttonEntities == null || StringUtils.isBlank(buttonCode)) return false;
        for (MenuButtonEntity entity : buttonEntities) {
            if (entity.getButtonCode() != null && entity.getButtonCode().equals(buttonCode)) {
                needAuth = true;
            }
        }
        return needAuth;
    }

    /**
     * 判断是否有方法权限
     *
     * @param url
     * @param methodName
     * @return
     */
    public static boolean hasMethodAuth(String url, String methodName) {
        boolean needAtuh = false;
        boolean success = false;
        if (!hasUrlAuth(url)) return false;
        SysMenuEntity menuEntity = ClientManager.getInstance().getCurrentClient().getMenuEntityMap().get(url + "?" + methodName);
        if (menuEntity == null) return true;
        List<MenuButtonEntity> buttonEntities = menuEntity.getButtonEntities();
        if (buttonEntities == null || StringUtils.isBlank(methodName)) return true;
        for (MenuButtonEntity entity : buttonEntities) {
            if (entity.getButtonCode() != null && entity.getMethodName().equals(methodName)) {
                needAtuh = true;
                if (entity.isHasPermission())
                    success = true;
            }
        }
        return (!needAtuh) || success;
    }

    /**
     * 判断请求是否有权限
     *
     * @param request
     * @return
     */
    public static boolean hasReuqestAuth(HttpServletRequest request) {
        String param = "";
        param = request.getQueryString();
        return hasMethodAuth(ResourceUtil.getRequestPath(request, false), param);
    }
}
