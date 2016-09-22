package com.code.service.impl;

import com.code.entity.CodeTemplateSettingEntity;
import com.code.service.CodeTemplateSettingService;
import com.sys.service.impl.CommonService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("codeTemplateSettingService")
@Transactional
public class CodeTemplateSettingServiceImpl extends CommonService implements CodeTemplateSettingService {

    @Override
    public CodeTemplateSettingEntity getSetting(String id) {
        if(StringUtils.isNotBlank(id)){
            return getEntity(CodeTemplateSettingEntity.class,id);
        }else{
            List<CodeTemplateSettingEntity> list=getDataListByHql("from CodeTemplateSettingEntity",new Object[0]);
            if(CollectionUtils.isNotEmpty(list))
                return list.get(0);
        }
        return null;

    }
}
