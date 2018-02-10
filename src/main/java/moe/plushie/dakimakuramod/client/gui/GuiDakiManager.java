package moe.plushie.dakimakuramod.client.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.gui.GuiDropDownList.IDropDownListCallback;
import moe.plushie.dakimakuramod.client.texture.DakiTexture;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.proxies.ClientProxy;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class GuiDakiManager extends GuiScreen implements IDropDownListCallback {
    
    protected final int guiWidth;
    protected final int guiHeight;
    
    protected int guiLeft;
    protected int guiTop;
    
    private GuiButtonExt buttonReloadTextures;
    private GuiDropDownList dropDownTextureSize;
    
    public GuiDakiManager() {
        this.guiWidth = 320;
        this.guiHeight = 240;
    }
    
    @Override
    public void initGui() {
        guiLeft = width / 2 - guiWidth / 2;
        guiTop = height / 2 - guiHeight / 2;
        
        buttonList.clear();
        
        buttonReloadTextures = new GuiButtonExt(0, guiLeft + guiWidth - 105, guiTop + 5, 100, 20, "Reload Textures");
        buttonList.add(buttonReloadTextures);
        
        dropDownTextureSize = new GuiDropDownList(0, guiLeft + guiWidth - 85, guiTop + 30, 80, String.valueOf(ConfigHandler.textureMaxSize), this);
        int maxGpuTexture = ((ClientProxy)DakimakuraMod.getProxy()).getMaxGpuTextureSize();
        for (int i = 5; i < 14; i++) {
            int size= (int) Math.pow(2, i);
            if (size > maxGpuTexture) {
                break;
            }
            dropDownTextureSize.addListItem(String.valueOf(size));
        }
        buttonList.add(dropDownTextureSize);
    }
    
    @Override
    protected void keyTyped(char key, int keycode) {
        if (keycode == Keyboard.KEY_ESCAPE || keycode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
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
        if (button == buttonReloadTextures) {
            ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().reloadTextures();
        }
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
        
        //fontRendererObj.drawString("Loaded textures: " + DakimakuraMod.getProxy().getTextureManagerCommon().size(), guiLeft + guiWidth - 110, guiTop + guiHeight - 14, 0xDDDDDD);
    }

    @Override
    public void onDropDownListChanged(GuiDropDownList dropDownList) {
        int size = Integer.parseInt(dropDownList.getListSelectedItem().displayText);
        ConfigHandler.textureMaxSize = size;
        ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().reloadTextures();
    }
}
