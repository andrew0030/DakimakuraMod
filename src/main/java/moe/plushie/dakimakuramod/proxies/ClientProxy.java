package moe.plushie.dakimakuramod.proxies;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.handler.PlacementPreviewHandler;
import moe.plushie.dakimakuramod.client.handler.RehostedJarHandler;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.client.render.entity.RenderEntityDakimakura;
import moe.plushie.dakimakuramod.client.render.item.RenderItemDakimakura;
import moe.plushie.dakimakuramod.client.render.tileentity.RenderBlockDakimakura;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManagerClient;
import moe.plushie.dakimakuramod.common.UpdateCheck;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.pack.IDakiPack;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    private DakiTextureManagerClient dakiTextureManager;
    private int maxGpuTextureSize;
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new UpdateCheck();
        new RehostedJarHandler(event.getSourceFile(), "dakimakuramod-" + LibModInfo.VERSION + ".jar");
    }
    
    @Override
    public void initRenderers() {
        dakiTextureManager = new DakiTextureManagerClient();
        ModelDakimakura modelDakimakura = new ModelDakimakura(dakiTextureManager);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockDakimakura), new RenderItemDakimakura(modelDakimakura));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDakimakura.class, new RenderBlockDakimakura(modelDakimakura));
        RenderingRegistry.registerEntityRenderingHandler(EntityDakimakura.class, new RenderEntityDakimakura(modelDakimakura));
        maxGpuTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        DakimakuraMod.getLogger().info(String.format("Max GPU texture size: %d.", maxGpuTextureSize));
        new PlacementPreviewHandler(modelDakimakura);
    }
    
    @Override
    public MinecraftServer getServer() {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return Minecraft.getMinecraft().getIntegratedServer();
        }
        return super.getServer();
    }
    
    @Override
    public void setDakiList(ArrayList<IDakiPack> packs) {
        super.setDakiList(packs);
        dakiTextureManager.reloadTextures();
    }
    
    public DakiTextureManagerClient getDakiTextureManager() {
        return dakiTextureManager;
    }
    
    public int getMaxGpuTextureSize() {
        return maxGpuTextureSize;
    }
}
