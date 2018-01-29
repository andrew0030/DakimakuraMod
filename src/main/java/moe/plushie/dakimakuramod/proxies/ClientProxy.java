package moe.plushie.dakimakuramod.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.client.render.item.RenderItemDakimakura;
import moe.plushie.dakimakuramod.client.render.tileentity.RenderBlockDakimakura;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    @Override
    public void initRenderers() {
        ModelDakimakura modelDakimakura = new ModelDakimakura();
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(getModBlocks().BLOCK_DAKIMAKURA), new RenderItemDakimakura(modelDakimakura));
        //RenderingRegistry.registerEntityRenderingHandler(EntityDakimakura.class, new RenderEntityDakimakura(modelDakimakura));
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.mannequin), new RenderItemMannequin(modelSteve, modelAlex));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDakimakura.class, new RenderBlockDakimakura(modelDakimakura));
    }
    
    @Override
    public MinecraftServer getServer() {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return Minecraft.getMinecraft().getIntegratedServer();
        }
        return super.getServer();
    }
}
