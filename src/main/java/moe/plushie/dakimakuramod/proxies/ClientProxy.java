package moe.plushie.dakimakuramod.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.client.render.item.RenderItemDakimakura;
import moe.plushie.dakimakuramod.client.render.tileentity.RenderBlockDakimakura;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManager;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    private DakiTextureManager dakiTextureManager;
    
    @Override
    public void initRenderers() {
        dakiTextureManager = new DakiTextureManager();
        ModelDakimakura modelDakimakura = new ModelDakimakura(dakiTextureManager);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockDakimakura), new RenderItemDakimakura(modelDakimakura));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDakimakura.class, new RenderBlockDakimakura(modelDakimakura));
        //DakimakuraMod.logger.info("Max texture size: " + GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
    }
    
    @Override
    public MinecraftServer getServer() {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return Minecraft.getMinecraft().getIntegratedServer();
        }
        return super.getServer();
    }
    
    public DakiTextureManager getDakiTextureManager() {
        return dakiTextureManager;
    }
}
