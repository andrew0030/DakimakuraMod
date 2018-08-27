package moe.plushie.dakimakuramod.proxies;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.handler.PlacementPreviewHandler;
import moe.plushie.dakimakuramod.client.handler.RehostedJarHandler;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.client.render.entity.RenderEntityDakimakura;
import moe.plushie.dakimakuramod.client.render.item.RenderItemDakimakura;
import moe.plushie.dakimakuramod.client.render.tileentity.RenderBlockDakimakura;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManagerClient;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import moe.plushie.dakimakuramod.common.items.ModItems;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibModInfo.ID, value = { Side.CLIENT })
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    private DakiTextureManagerClient dakiTextureManager;
    private int maxGpuTextureSize;
    public static ModelDakimakura modelDakimakura;
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new RehostedJarHandler(event.getSourceFile(), "dakimakuramod-" + LibModInfo.VERSION + ".jar");
    }
    
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomMeshDefinition(ModItems.dakiDesign, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                Daki daki = DakiNbtSerializer.deserialize(stack.getTagCompound());
                if (daki == null) {
                    return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
                } else {
                    return new ModelResourceLocation(stack.getItem().getRegistryName() + "-unlock", "inventory");
                }
            }
        });
        ModelBakery.registerItemVariants(ModItems.dakiDesign, new ModelResourceLocation(ModItems.dakiDesign.getRegistryName(), "inventory"), new ModelResourceLocation(ModItems.dakiDesign.getRegistryName() + "-unlock", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModBlocks.itemBlockDakimakura, 0, new ModelResourceLocation(new ResourceLocation(LibModInfo.ID, "item.dakimakura"), "inventory"));
        Item.getItemFromBlock(ModBlocks.blockDakimakura).setTileEntityItemStackRenderer(new RenderItemDakimakura(modelDakimakura));
    }
    
    @Override
    public void preInitRenderers() {
        dakiTextureManager = new DakiTextureManagerClient();
        modelDakimakura = new ModelDakimakura(dakiTextureManager);
        RenderingRegistry.registerEntityRenderingHandler(EntityDakimakura.class, new IRenderFactory() {

            @Override
            public Render createRenderFor(RenderManager manager) {
                return new RenderEntityDakimakura(Minecraft.getMinecraft().getRenderManager(), modelDakimakura);
            }
        });
    }
    
    @Override
    public void initRenderers() {
        ModBlocks.itemBlockDakimakura.setTileEntityItemStackRenderer(new RenderItemDakimakura(modelDakimakura));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDakimakura.class, new RenderBlockDakimakura(modelDakimakura));
        maxGpuTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        DakimakuraMod.getLogger().info(String.format("Max GPU texture size: %d.", maxGpuTextureSize));
        new PlacementPreviewHandler(modelDakimakura);
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
