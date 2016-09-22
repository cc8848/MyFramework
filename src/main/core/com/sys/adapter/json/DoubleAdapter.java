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
public class DoubleAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter jsonWriter, Double aDouble) throws IOException {
        if(aDouble==null)
            jsonWriter.value(0f);
        else
            jsonWriter.value(aDouble);
    }

    @Override
    public Double read(JsonReader jsonReader) throws IOException {
        String str=jsonReader.nextString();
        if(StringUtils.isBlank(str)||"null".equals(str))
            return 0.0;
        else
            return new Double(str);
    }
}
