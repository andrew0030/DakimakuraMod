package moe.plushie.dakimakuramod.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.texture.DakiTexture;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.proxies.ClientProxy;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class GuiDakiManager extends GuiScreen {
    
    protected final int guiWidth;
    protected final int guiHeight;
    
    protected int guiLeft;
    protected int guiTop;
    
    public GuiDakiManager() {
        this.guiWidth = 320;
        this.guiHeight = 240;
    }
    
    @Override
    public void initGui() {
        guiLeft = width / 2 - guiWidth / 2;
        guiTop = height / 2 - guiHeight / 2;
        
        buttonList.clear();
    }
    
    @Override
    protected void keyTyped(char key, int keycode) {
        if (keycode == 1 || keycode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
        super.keyTyped(key, keycode);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_) {
        drawGradientRect(this.guiLeft, this.guiTop, this.guiLeft + this.guiWidth, this.guiTop + guiHeight, 0xC0101010, 0xD0101010);
        super.drawScreen(mouseX, mouseY, p_73863_3_);
        fontRendererObj.drawString("Not Finished!", guiLeft + 5, guiTop + 5, 0xDDDDDD);
        Daki hoverDaki = null;
        DakiManager dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
        ArrayList<Daki> dakiList = dakiManager.getDakiList();
        for (int i = 0; i < dakiList.size(); i++) {
            Daki daki = dakiList.get(i);
            int colour = 0xCCCCCC;
            if (mouseX >= guiLeft + 5 & mouseX < guiLeft + 110 & mouseY >= guiTop + 20 + i * 12 & mouseY < guiTop + 28 + i * 12) {
                colour = 0xFFFFFF;
                hoverDaki = daki;
            }
            DakiTexture dakiTexture = ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().getTextureForDaki(daki);
            if (dakiTexture.isLoaded()) {
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, dakiTexture.getGlTextureId());
                Gui.func_152125_a(guiLeft + 5, guiTop + 20 + i * 12, 0, 0, 1, 1, 10, 10, 1, 1);
            }
            fontRendererObj.drawString(daki.getDakiDirectoryName(), guiLeft + 18, guiTop + 20 + i * 12, colour, true);
        }
        
        if (hoverDaki != null) {
            DakiTexture dakiTexture = ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().getTextureForDaki(hoverDaki);
            if (dakiTexture.isLoaded()) {
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, dakiTexture.getGlTextureId());
                Gui.func_152125_a(guiLeft + 120, guiTop + 10, 0, 0, 1, 1, 190, 190, 1, 1);
            }
        }
    }
}
