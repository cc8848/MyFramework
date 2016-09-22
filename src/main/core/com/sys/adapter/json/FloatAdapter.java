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
public class FloatAdapter extends TypeAdapter<Float> {
    @Override
    public void write(JsonWriter jsonWriter, Float aFloat) throws IOException {
        if(aFloat==null)
            jsonWriter.value(0f);
        else
            jsonWriter.value(aFloat);
    }

    @Override
    public Float read(JsonReader jsonReader) throws IOException {
        String str=jsonReader.nextString();
        if(StringUtils.isBlank(str)||"null".equals(str))
            return 0f;
        else
            return new Float(str);
    }
}
