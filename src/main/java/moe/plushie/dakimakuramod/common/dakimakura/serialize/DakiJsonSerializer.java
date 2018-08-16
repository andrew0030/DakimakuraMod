package moe.plushie.dakimakuramod.common.dakimakura.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;

public final class DakiJsonSerializer {

    private DakiJsonSerializer() {}
    
    public static JsonElement serialize(Daki src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("author", src.getAuthor());
        if (src.getImageFront() != null) {
            jsonObject.addProperty("image-front", src.getImageFront());
        }
        if (src.getImageBack() != null) {
            jsonObject.addProperty("image-back", src.getImageBack());
        }
        jsonObject.addProperty("flavour-text", src.getFlavourText());
        jsonObject.addProperty("smooth", src.isSmooth());
        return jsonObject;
    }
    
    public static Daki deserialize(String jsonString, String packDirectoryName, String dakiDirectoryName) {
        if (jsonString == null) {
            return null;
        }
        try {
            JsonParser parser = new JsonParser();
            return deserialize(parser.parse(jsonString), packDirectoryName, dakiDirectoryName); 
        } catch (Exception e) {
            DakimakuraMod.getLogger().error("Error loadingt daki " + packDirectoryName + " " + dakiDirectoryName);
            DakimakuraMod.getLogger().error(e.getLocalizedMessage());
            return null;
        }
    }
    
    public static Daki deserialize(JsonElement json, String packDirectoryName, String dakiDirectoryName) {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement nameElement = jsonObject.get("name");
            JsonElement authorElement = jsonObject.get("author");
            JsonElement imageFrontElement = jsonObject.get("image-front");
            JsonElement imageBackElement = jsonObject.get("image-back");
            JsonElement flavourTextElement = jsonObject.get("flavour-text");
            JsonElement smoothElement = jsonObject.get("smooth");
            Daki dakimakura = new Daki(packDirectoryName, dakiDirectoryName);
            if (authorElement != null) {
                dakimakura.setAuthor(authorElement.getAsString());
            }
            if (nameElement != null) {
                dakimakura.setName(nameElement.getAsString());
            }
            if (imageFrontElement != null) {
                dakimakura.setImageFront(imageFrontElement.getAsString());
            } else {
                dakimakura.setImageFront(null);
            }
            if (imageBackElement != null) {
                dakimakura.setImageBack(imageBackElement.getAsString());
            } else {
                dakimakura.setImageFront(null);
            }
            if (flavourTextElement != null) {
                dakimakura.setFlavourText(flavourTextElement.getAsString());
            }
            if (smoothElement != null) {
                dakimakura.setSmooth(smoothElement.getAsBoolean());
            }
            return dakimakura;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
