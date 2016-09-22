package com.code.util;

import com.code.entity.ModuleExtDetailEntity;
import com.code.entity.ModuleExtEntity;
import com.code.service.ModuleExtService;
import com.sys.annotation.Ehcache;
import com.sys.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 组件工具类
 */
@Component
public class ModulesUtil {
    private static String MODULESLINKROOT="/modules/";
    private static ModulesUtil modulesUtil;
    @Resource
    private ModuleExtService moduleExtService;
    @PostConstruct
    public void init(){
        modulesUtil=this;
        modulesUtil.moduleExtService=this.moduleExtService;
    }

    /**
     * 通过表名获取组件信息
     * @param tableName
     * @return
     */
    public static List<ModuleExtEntity>  getModulesLink(String tableName){
        if(StringUtils.isBlank(tableName))
            return new ArrayList<>();
       List<ModuleExtEntity> list= modulesUtil.moduleExtService.getModuleExtList(tableName,true);
       return list;
    }
}
