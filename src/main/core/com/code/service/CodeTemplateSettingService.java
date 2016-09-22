package com.code.service;

import com.code.entity.CodeTemplateSettingEntity;
import com.sys.service.ICommonService;
public interface CodeTemplateSettingService  extends ICommonService {
    /**
     * 获取设置信息
     * 若id为null则取第一条记录
     * @return
     */
    public CodeTemplateSettingEntity getSetting(String id);
}
