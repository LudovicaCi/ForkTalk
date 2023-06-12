package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer implements JsonDeserializer<Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String dateStr = jsonElement.getAsString();
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new JsonParseException("Failed to parse date: " + dateStr, e);
        }
    }
}
