package com.github.andrew0030.dakimakuramod.dakimakura.serialize;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * Helper class to more easily handle storing and retrieving values, to and from a {@link JsonElement}
 */
public final class DakiJsonSerializer
{
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Converts the given {@link Daki} to a {@link JsonElement}
     * @param src The {@link Daki} that will be used to set up the {@link JsonElement} properties
     * @return A {@link JsonElement} holding properties based on the given {@link Daki}
     */
    public static JsonElement serialize(Daki src)
    {
        JsonObject jsonObject = new JsonObject();
        if (src.getImageFront() != null)
            jsonObject.addProperty("image-front", src.getImageFront());
        if (src.getImageBack() != null)
            jsonObject.addProperty("image-back", src.getImageBack());
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("author", src.getAuthor());
        jsonObject.addProperty("flavour-text", src.getFlavourText());
        jsonObject.addProperty("smooth", src.isSmooth());
        return jsonObject;
    }

    /**
     * Creates a {@link Daki} from a Json
     * @param jsonString A {@link String} that will be parsed into a {@link JsonElement}
     * @param packDirectoryName The {@link Daki} pack name
     * @param dakiDirectoryName The {@link Daki} directory name
     * @return A {@link Daki} based on the properties of the given Json
     */
    public static Daki deserialize(String jsonString, String packDirectoryName, String dakiDirectoryName)
    {
        if (jsonString == null) return null;
        try {
            return DakiJsonSerializer.deserialize(JsonParser.parseString(jsonString), packDirectoryName, dakiDirectoryName);
        } catch (Exception e) {
            LOGGER.error(String.format("Error loading Daki '%s' '%s'", packDirectoryName, dakiDirectoryName));
            LOGGER.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Creates a {@link Daki} from a {@link JsonElement}
     * @param json A {@link JsonElement} which holds the daki properties
     * @param packDirectoryName The {@link Daki} pack name
     * @param dakiDirectoryName The {@link Daki} directory name
     * @return A {@link Daki} based on the properties of the given Json
     */
    public static Daki deserialize(JsonElement json, String packDirectoryName, String dakiDirectoryName)
    {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement nameElement = jsonObject.get("name");
            JsonElement authorElement = jsonObject.get("author");
            JsonElement imageFrontElement = jsonObject.get("image-front");
            JsonElement imageBackElement = jsonObject.get("image-back");
            JsonElement flavourTextElement = jsonObject.get("flavour-text");
            JsonElement smoothElement = jsonObject.get("smooth");

            Daki dakimakura = new Daki(packDirectoryName, dakiDirectoryName);
            dakimakura.setImageFront(imageFrontElement == null ? null : imageFrontElement.getAsString());
            dakimakura.setImageBack(imageBackElement == null ? null : imageBackElement.getAsString());
            if (authorElement != null)
                dakimakura.setAuthor(authorElement.getAsString());
            if (nameElement != null)
                dakimakura.setName(nameElement.getAsString());
            if (flavourTextElement != null)
                dakimakura.setFlavourText(flavourTextElement.getAsString());
            if (smoothElement != null)
                dakimakura.setSmooth(smoothElement.getAsBoolean());
            return dakimakura;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}