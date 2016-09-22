package com.code.service.impl;

import com.code.entity.TableFieldBean;
import com.code.service.ITableFieldService;
import com.sys.service.impl.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zzl
 *         Date:2014-08-07
 */
@Service("tableFieldService")
@Transactional
public class TableFieldService extends CommonService implements ITableFieldService {

    @Override
    public List<TableFieldBean> findFileField(String tableName) {
        return getDataListByHql("select bean from TableFieldBean bean where bean.head.tableName=? and fieldType='file'",new Object[]{tableName});
    }
}
