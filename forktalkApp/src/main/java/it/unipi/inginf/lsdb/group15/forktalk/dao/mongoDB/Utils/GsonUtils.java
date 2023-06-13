package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils;

import com.google.gson.*;
import it.unipi.inginf.lsdb.group15.forktalk.dto.ReviewDTO;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GsonUtils implements JsonDeserializer<Date> {
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

    public static Date parseTimestamp(String timestampString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(timestampString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> jsonElementToStringArrayList(JsonElement jsonElement) {
        ArrayList<String> stringList = new ArrayList<>();

        if (jsonElement != null && jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                stringList.add(element.getAsString());
            }
        }

        return stringList;
    }
}
