package com.basetemplate.service.impl;

import com.basetemplate.service.TemplateService;
import com.code.service.ITableFieldService;
import com.sys.service.impl.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zzl
 *         Date:2014-08-07
 */
@Service("templateService")
@Transactional
public class TemplateServiceImpl extends CommonService implements TemplateService {
}
