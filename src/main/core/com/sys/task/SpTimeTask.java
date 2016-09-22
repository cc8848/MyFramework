package com.sys.task;

/**
 * Created by sun on 2015/4/14.
 */

import com.sys.constant.Globals;
import com.sys.entity.SessionUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 审批超时预警
 */
@Service("spTask")
public class SpTimeTask {

  //   @Scheduled(cron="0 0/1 * * * ?")
    public void doSp(){
        System.out.println("==============>审批超时预警！");
    }



}
