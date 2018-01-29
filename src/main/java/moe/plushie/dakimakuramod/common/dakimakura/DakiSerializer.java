package moe.plushie.dakimakuramod.common.dakimakura;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class DakiSerializer {

    private DakiSerializer() {}
    
    public static JsonElement serialize(Daki src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("romaji-name", src.getRomajiName());
        jsonObject.addProperty("original-name", src.getOriginalName());
        jsonObject.addProperty("author", src.getAuthor());
        return jsonObject;
    }
    
    public static Daki deserialize(String jsonString, String packDirectoryName, String dakiDirectoryName) {
        if (jsonString == null) {
            return null;
        }
        JsonParser parser = new JsonParser();
        return deserialize(parser.parse(jsonString), packDirectoryName, dakiDirectoryName); 
    }
    
    public static Daki deserialize(JsonElement json, String packDirectoryName, String dakiDirectoryName) {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement romajiNameElement = jsonObject.get("romaji-name");
            JsonElement originalNameElement = jsonObject.get("original-name");
            JsonElement authorElement = jsonObject.get("author");
            JsonElement imageFrontElement = jsonObject.get("image-front");
            JsonElement imageBackElement = jsonObject.get("image-back");
            Daki dakimakura = new Daki(packDirectoryName, dakiDirectoryName);
            dakimakura.setAuthor(authorElement.getAsString());
            dakimakura.setRomajiName(romajiNameElement.getAsString());
            dakimakura.setOriginalName(originalNameElement.getAsString());
            dakimakura.setImageFront(imageFrontElement.getAsString());
            dakimakura.setImageBack(imageBackElement.getAsString());
            return dakimakura;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
