package com.sys.interceptors;

import com.code.service.ITableInfService;
import com.sys.constant.Globals;
import com.sys.entity.*;
import com.sys.manager.ClientManager;
import com.sys.service.SysDepService;
import com.sys.service.SysMenuService;
import com.sys.service.SysRoleService;
import com.sys.service.SysUserService;
import com.sys.util.AuthUtil;
import com.sys.util.ResourceUtil;
import com.sys.util.SuccessMsg;
import com.sys.util.WebUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 权限拦截器
 * @author zzl
 * Date:2014-08-30
 */
public class AuthInterceptor implements HandlerInterceptor {
    private List<String> excludeUrls;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;


    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysDepService sysDepService;

    @Resource
    private ITableInfService tableInfService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        boolean success=true;
        SessionUser sessionUser= (SessionUser) request.getSession().getAttribute(Globals.SESSIONUSE);

        if(sessionUser==null)
            ClientManager.getInstance().removeClient(request.getSession().getId());

        if(excludeUrls!=null&&excludeUrls.size()>0){
            String url=ResourceUtil.getRequestPath(request,true);

        //采集，查看 路径
if(url.contains("front/yw_file.do?showCj")||url.contains("archives_cj/cj.do?showCj")||
        url.contains("front/yw_file.do?getXmlInfo")||url.contains("archives_cj/cj.do?showPic")||url.contains("front/yw_file.do?uploadForFtp")) {
   if( sessionUser==null)
   {
       String  userKey="123";
       SysUserEntity entity = sysUserService.findByUserKey(userKey);
       HttpSession session = request.getSession();
       session.setAttribute(Globals.SESSIONUSE, new SessionUser(entity));
       ClientManager.getInstance().addClient(session.getId(), getClient(entity, request));
   }
    return true;
}

            if((url.startsWith("front")||url.contains("dajs_tjjs")|| url.contains("luceneSearch")||url.contains("cj"))&&(url.contains("uploadClList")||url.contains("uploadClList")||url.contains("download")||url.contains("searchKeys")))
            {

                    String  userKey="123";
                    SysUserEntity entity = sysUserService.findByUserKey(userKey);
                    HttpSession session = request.getSession();
                    session.setAttribute(Globals.SESSIONUSE, new SessionUser(entity));
                    ClientManager.getInstance().addClient(session.getId(), getClient(entity, request));
                    // url=url.replace("userid="+userKey+"&","");
                    //      response.sendRedirect(request.getContextPath() +"/"+ url);
                    return true;
                }


            if(url.contains("datagrid") || url.contains("luceneSearch") )
            {
//                sysOperRecordService.queryRecord(url);
            }


            for(String str:excludeUrls){
                if(str.endsWith("*")){
                    if(url.startsWith(str.replace("*",""))){
                        return true;
                    }
                }else if(str.equals(url)){
                    return true;
                }
            }
        }

        Client client= ClientManager.getInstance().getCurrentClient();
        if(client==null||client.getUserEntity()==null) {
            success= false;
        }
     // success=success&&AuthUtil.hasReuqestAuth(request);
        if(!success){
            request.setAttribute(Globals.SYSTEM_MSG,new SuccessMsg(false,"用户已超时或无访问权限"));
            response.sendRedirect(request.getContextPath()+"/login.do?login");
        }
        return success;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
    private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("webpage/login.jsp").forward(request, response);
    }
    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    /**
     * 拦截查询，并记录到数据库
     *
     */

//    public void queryRecord(String url)
//    {
//        SessionUser user=WebUtil.getSessionUser();
//        String name=user.getUserName();//用户名
//        Date time=new Date();//时间
//                  //查询信息
//       String operTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
//        SysOperRecordEntity a=new SysOperRecordEntity();
//        a.setOpername(name);
//        a.setOpertime(operTime);
//
//        //截取url
//        String s=  url.substring(url.lastIndexOf("/")+1, url.indexOf("."));
//
//    //   String content= tableInfService.getTableContentTo("business_cslh");//测试
//        String content= tableInfService.getTableContentTo(s);
//        if(content!=null && content.length()>0){
//            a.setTablename(content);
//        }else{
//            a.setTablename(url);
//        }
//      //  a.setTablename(url);
//        sysOperRecordService.save(a);
//    }


    /**
     * 生成在线用户信息
     * @param entity
     * @param request
     * @return
     */
    private Client getClient(SysUserEntity entity,HttpServletRequest request){
        Client client=new Client();
        List<SysRoleEntity> roleEntityList=sysRoleService.getRolesByUserId(entity.getId());
        client.setUserEntity(entity);
        client.setRoleEntities(roleEntityList);
        client.setLoginTime(new Date());
        client.setIpAddress(ResourceUtil.getIpAddr(request));
        if(roleEntityList!=null){
            List<Long> roleIds=new ArrayList<>();
            for(SysRoleEntity roleEntity:roleEntityList){
                roleIds.add(roleEntity.getId());
            }
            List<Long> childRoleIds=sysRoleService.getChildrenId(roleIds,false);
            List<SysMenuEntity> menuEntityList=sysMenuService.getRoleMenuAndButtons(childRoleIds);
            Set<Long> menuSetId=new HashSet<>();
            if(menuEntityList!=null){
                Map<String,SysMenuEntity> map=new HashMap<>();
                for(SysMenuEntity entity1:menuEntityList){
                   /* if(entity1.getMenuLink()!=null&&entity1.getMenuLink().indexOf("?")!=-1)
                        map.put(entity1.getMenuLink().substring(0,entity1.getMenuLink().indexOf("?")),entity1);
                    else*/
                    menuSetId.add(entity1.getId());
                    map.put(entity1.getMenuLink(),entity1);
                }
                client.setMenuEntityMap(map);
                client.setMenuIdSet(menuSetId);
            }

        }

        SysDepEntity depEntity=entity.getDep();
        if(depEntity!=null){
            // client.addDataFilter(getDataFilter(depEntity.getId()));
        }
        return client;
    }


    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
