package com.sys.adapter.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * Created by lenovo on 2015/1/19.
 */
public class LongAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter jsonWriter, Long integer) throws IOException {
        if(integer==null)
            jsonWriter.value(0);
        else
            jsonWriter.value(integer);
    }

    @Override
    public Long read(JsonReader jsonReader) throws IOException {
        String str=jsonReader.nextString();
        if(StringUtils.isBlank(str)||"null".equals(str))
            return 0l;
        else
            return new Long(str);
    }
}
