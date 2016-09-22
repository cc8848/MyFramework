package com.sys.adapter.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sys.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zzl
 *         Date:2014-09-17
 */
public class DateAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        SimpleDateFormat sdf=new SimpleDateFormat(DateUtil.YYYYMMDD);
        if(date!=null)
            jsonWriter.value(sdf.format(date));
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        String str=jsonReader.nextString();
        if(StringUtils.isBlank(str)||"null".equals(str))
            return null;
        else{
            SimpleDateFormat sdf=new SimpleDateFormat(DateUtil.YYYYMMDD);
            Date d=null;
            try {
                d=sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return d;
        }

    }
}
