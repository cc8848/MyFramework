package com.sys.adapter.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * @author zzl
 *         Date:2014-08-11
 */
public class IntegerAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(JsonWriter jsonWriter, Integer integer) throws IOException {
        if(integer==null)
            jsonWriter.value(0);
        else
            jsonWriter.value(integer);
    }

    @Override
    public Integer read(JsonReader jsonReader) throws IOException {
        String str=jsonReader.nextString();
        if(StringUtils.isBlank(str)||"null".equals(str))
            return 0;
        else
        return new Integer(str);
    }
}
