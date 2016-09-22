package com.sys.task;

import org.springframework.stereotype.Service;

/**
 * 资源回收
 */
@Service("gcTask")
public class GcTask {
    public void doGc(){
       Runtime.getRuntime().gc();
        System.out.println("==============>内存回收！");
    }
}
