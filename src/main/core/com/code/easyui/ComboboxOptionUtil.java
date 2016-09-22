package com.code.easyui;

import com.google.gson.Gson;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * combobox option 解析
 * @author zzl
 * Date:2014-08-22
 */
public class ComboboxOptionUtil {
    /**
     * 将 形如 1_是,2_否 这样的数据转变成bean
     * @param str
     * @return
     */
    public static List<ComboboxOptionBean> fromString(String str){
        List<ComboboxOptionBean> list=new ArrayList<>();
        if(StringUtils.isNotBlank(str)){
            String[] strs=str.split(",");
               if(strs!=null){
                    for(String s :strs){
                        String[] strs2=s.split("_");
                        if(strs2!=null){
                            if(strs2.length==1)
                                list.add(new ComboboxOptionBean(strs2[0],strs2[0]));
                            if(strs2.length==2)
                                list.add(new ComboboxOptionBean(strs2[0],strs2[1]));
                        }
                    }
               }
        }
        return list;
    }

    /**
     * 转换成options data
     * @param str
     * @return
     */
    public static String getData(String str){
        if(StringUtils.isBlank(str))
            return "";
        return (new Gson().toJson(fromString(str))).replace("\"","'");
    }

    /**
     * 由list转成options data
     * @param list
     * @return
     */
    public static String getDataByList(List<ComboboxOptionBean> list){
        return (new Gson().toJson(list)).replace("\"","'");
    }
}
