package moe.plushie.dakimakuramod.proxies;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.handler.PlacementPreviewHandler;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.client.render.tileentity.RenderBlockDakimakura;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManagerClient;
import moe.plushie.dakimakuramod.common.UpdateCheck;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.ModItems;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    private DakiTextureManagerClient dakiTextureManager;
    private int maxGpuTextureSize;
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new UpdateCheck();
    }
    
    @Override
    public void preInitRenderers() {
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.blockDakimakura), 0, TileEntityDakimakura.class);
        ModelLoader.setCustomMeshDefinition(ModItems.dakiDesign, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                Daki daki = DakiNbtSerializer.deserialize(stack.getTagCompound());
                if (daki == null) {
                    return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
                } else {
                    return new ModelResourceLocation(stack.getItem().getRegistryName() + "Unlock", "inventory");
                }
            }
        });
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(ModBlocks.blockDakimakura), new ItemMeshDefinition() {
            
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                Daki daki = DakiNbtSerializer.deserialize(stack.getTagCompound());
                RenderBlockDakimakura.lastItemDaki = daki;
                return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
            }
        });
        
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.blockDakimakura), 0, new ModelResourceLocation(LibModInfo.ID + ":tile.dakimakura", "inventory"));
    }
    
    @Override
    public void initRenderers() {
        dakiTextureManager = new DakiTextureManagerClient();
        ModelDakimakura modelDakimakura = new ModelDakimakura(dakiTextureManager);

        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockDakimakura), new RenderItemDakimakura(modelDakimakura));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDakimakura.class, new RenderBlockDakimakura(modelDakimakura));
        //RenderingRegistry.registerEntityRenderingHandler(EntityDakimakura.class, new RenderEntityDakimakura(modelDakimakura));
        maxGpuTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        DakimakuraMod.getLogger().info(String.format("Max GPU texture size: %d.", maxGpuTextureSize));
        new PlacementPreviewHandler(modelDakimakura);
        ModelBakery.registerItemVariants(ModItems.dakiDesign, new ModelResourceLocation(ModItems.dakiDesign.getRegistryName(), "inventory"), new ModelResourceLocation(ModItems.dakiDesign.getRegistryName() + "Unlock", "inventory"));
        //registerRender(ModItems.dakiDesign);
    }
    
    @Override
    public MinecraftServer getServer() {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return Minecraft.getMinecraft().getIntegratedServer();
        }
        return super.getServer();
    }
    
    @Override
    public void setDakiList(ArrayList<Daki> dakiList) {
        super.setDakiList(dakiList);
        dakiTextureManager.reloadTextures();
    }
    
    public DakiTextureManagerClient getDakiTextureManager() {
        return dakiTextureManager;
    }
    
    public int getMaxGpuTextureSize() {
        return maxGpuTextureSize;
    }
    
    private void registerRender(Block block) {
        registerRender(block, 0);
    }
    
    private void registerRender(Block block, int meta) {
        registerRender(Item.getItemFromBlock(block), meta);
    }
    
    private void registerRender(Item item) {
        registerRender(item, 0);
    }
    
    private void registerRender(Item item, int meta) {
        ItemModelMesher imm = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        imm.register(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
