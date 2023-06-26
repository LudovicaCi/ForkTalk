package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GsonUtils implements JsonDeserializer<Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Deserializes a JSON element representing a date into a Date object.
     *
     * @param jsonElement The JSON element representing the date.
     * @param type The type of the object to deserialize.
     * @param context The JSON deserialization context.
     * @return The deserialized Date object.
     * @throws JsonParseException If the date string cannot be parsed.
     */
    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String dateStr = jsonElement.getAsString();
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new JsonParseException("Failed to parse date: " + dateStr, e);
        }
    }

    /**
     * Parses a timestamp string into a Date object.
     *
     * @param timestampString The timestamp string to parse.
     * @return The parsed Date object, or null if the timestamp string cannot be parsed.
     */
    public static Date parseTimestamp(String timestampString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(timestampString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a JsonElement object into an ArrayList of strings.
     *
     * @param jsonElement The JsonElement to convert.
     * @return An ArrayList of strings extracted from the JsonElement, or an empty ArrayList if the JsonElement is null or not a JsonArray.
     */
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
