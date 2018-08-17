package moe.plushie.dakimakuramod.client.handler;

import java.io.File;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.io.Charsets;

import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public final class RehostedJarHandler {
    
    private static final String STOP_MOD_REPOSTS_URL = "http://stopmodreposts.org/";
    
    private boolean validJar = false;
    
    private long lastMessagePost = 0;
    private long messagePostRate = 1000 * 120;
    
    public RehostedJarHandler(File jarFile, String originalName) {
        checkIfJarIsValid(jarFile, originalName);
        FMLCommonHandler.instance().bus().register(this);
    }
    
    private void checkIfJarIsValid(File jarFile, String originalName) {
        if (jarFile == null) {
            return;
        }
        
        if (jarFile.getName().equals(originalName)) {
            // Names match.
            validJar = true;
            return;
        }
        
        CharsetEncoder asciiEncoder = Charsets.US_ASCII.newEncoder();
        if (!asciiEncoder.canEncode(jarFile.getName())) {
            // Most likely posted on a local language site.
            validJar = true;
            return;
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (validJar) {
            return;
        }
        if (LibModInfo.isDevelopmentVersion()) {
            return;
        }
        if (event.side != Side.CLIENT) {
            return;
        }
        
        if (event.type != Type.PLAYER) {
            return;
        }
        if (event.phase != Phase.END) {
            return;
        }
        if (lastMessagePost + messagePostRate > System.currentTimeMillis()) {
            return;
        }
        lastMessagePost = System.currentTimeMillis();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        TextComponentTranslation downloadLink = new TextComponentTranslation("chat.dakimakuramod:invalidJarDownload", (Object)null);
        downloadLink.getStyle().setUnderlined(true);
        downloadLink.getStyle().setColor(TextFormatting.BLUE);
        downloadLink.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("chat.dakimakuramod:invalidJarDownloadTooltip", (Object)null)));
        downloadLink.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, LibModInfo.UPDATE_PAGE));
        
        TextComponentTranslation stopModRepostsLink = new TextComponentTranslation("chat.dakimakuramod:invalidJarStopModReposts", (Object)null);
        stopModRepostsLink.getStyle().setUnderlined(true);
        stopModRepostsLink.getStyle().setColor(TextFormatting.BLUE);
        stopModRepostsLink.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("chat.dakimakuramod:invalidJarStopModRepostsTooltip", (Object)null)));
        stopModRepostsLink.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, STOP_MOD_REPOSTS_URL));
        
        TextComponentTranslation updateMessage = new TextComponentTranslation("chat.dakimakuramod:invalidJar", downloadLink, stopModRepostsLink);
        updateMessage.getStyle().setColor(TextFormatting.RED);
        player.addChatMessage(updateMessage);
    }
}
