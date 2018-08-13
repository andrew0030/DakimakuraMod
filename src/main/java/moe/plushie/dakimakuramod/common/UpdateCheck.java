package moe.plushie.dakimakuramod.common;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;

public class UpdateCheck implements Runnable {
    
    /** The url to use for update checking */
    private static final String UPDATE_URL = "http://plushie.moe/app_update/minecraft_mods/dakimakuramod/update.json";
    
    private static String downloadUrl = "";
    
    private boolean shownUpdateInfo = false;
    
    /** Was an update found. */
    public boolean updateFound = false;
    
    public String remoteModVersion;
    
    public UpdateCheck() {
        checkForUpdates();
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.CLIENT) {
            if (event.type == Type.PLAYER) {
                if (event.phase == Phase.END) {
                    onPlayerTickEndEvent();
                }
            }
        }
    }
    
    public void onPlayerTickEndEvent() {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (!shownUpdateInfo && updateFound) {
            shownUpdateInfo = true;
            ChatComponentTranslation updateMessage = new ChatComponentTranslation("chat.dakimakuramod:updateAvailable", remoteModVersion);
            ChatComponentTranslation updateURL = new ChatComponentTranslation("chat.dakimakuramod:updateDownload");
            updateURL.getChatStyle().setUnderlined(true);
            updateURL.getChatStyle().setColor(EnumChatFormatting.BLUE);
            updateURL.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentTranslation("chat.dakimakuramod:updateDownloadRollover")));
            updateURL.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));
            updateMessage.appendText(" ");
            updateMessage.appendSibling(updateURL);
            player.addChatMessage(updateMessage);
        }
    }
    
    public void checkForUpdates() {
        if (!ConfigHandler.checkForUpdates) {
            return;
        }
        new Thread(this, LibModInfo.NAME + " update thread.").start();
    }
    
    @Override
    public void run() {
        DakimakuraMod.getLogger().info("Starting Update Check");
        String localVersion = LibModInfo.VERSION;
        
        //localVersion = "1.7.10-0.15";
        if (localVersion.equals("@VERSION@")) {
            return;
        }
        
        String downloadData = "";
        HttpURLConnection conn = null;
        try {
            String location = UPDATE_URL;
            while (location != null && !location.isEmpty()) {
                URL url = new URL(location);
                if (conn != null) {
                    conn.disconnect();
                }
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; ru; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 (.NET CLR 3.5.30729)");
                conn.setRequestProperty("Referer", "http://" + LibModInfo.VERSION);
                conn.connect();
                location = conn.getHeaderField("Location");
            }
            if (conn == null) {
                throw new NullPointerException();
            }
            downloadData = IOUtils.toString(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            DakimakuraMod.getLogger().warn("Unable to read from remote version authority.");
            DakimakuraMod.getLogger().warn(e.toString());
            updateFound = false;
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e2) {
                }
            }
        }
        
        if (StringUtils.isNullOrEmpty(downloadData)) {
            return;
        }
        
        try {
            JsonObject json = (JsonObject) new JsonParser().parse(downloadData);
            
            remoteModVersion = json.getAsJsonObject("promos").get(MinecraftForge.MC_VERSION + "-latest").getAsString();
            
            DakimakuraMod.getLogger().info("Home page: " + json.get("homepage").getAsString());
            downloadUrl = json.get("homepage").getAsString();
            
            DakimakuraMod.getLogger().info(String.format("Latest version for Minecraft %s is %s.", MinecraftForge.MC_VERSION, remoteModVersion));
            
            if (versionCompare(localVersion.replaceAll("-", "."), remoteModVersion.replaceAll("-", ".")) < 0) {
                updateFound = true;
                DakimakuraMod.getLogger().info("Update needed. New version " + remoteModVersion + " your version " + localVersion);
            } else {
                updateFound = false;
                DakimakuraMod.getLogger().info("Mod is up to date with the latest version.");
            }
            
        } catch (Exception e) {
            DakimakuraMod.getLogger().warn("Unable to read from remote version authority.");
            DakimakuraMod.getLogger().warn(e.toString());
            updateFound = false;
        }
    }
    
    private int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version
        // string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }
}
