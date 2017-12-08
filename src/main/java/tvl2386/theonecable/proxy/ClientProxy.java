package tvl2386.theonecable.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tvl2386.theonecable.Main;
import tvl2386.theonecable.objects.blocks.MyModelLoader;

public class ClientProxy extends CommonProxy {

    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        final int DEFAULT_ITEM_SUBTYPE = 0;

        // Models for Block3DWeb
        StateMapperBase stateBlock3DWeb = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation("theonecable:mbe05_block_3d_web_statemapper_name");
            }
        };

        ModelLoader.setCustomStateMapper(Main.proxy.block3DWeb, stateBlock3DWeb);
        ModelLoader.setCustomModelResourceLocation(Main.proxy.itemBlock3DWeb, DEFAULT_ITEM_SUBTYPE,
                new ModelResourceLocation("theonecable:mbe05_block_3d_web", "inventory"));




        // Models for BlockCable
        StateMapperBase stateBlockCable = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation("theonecable:cable_statemapper_name");
            }
        };

        ModelLoader.setCustomStateMapper(Main.proxy.blockCable, stateBlockCable);
        ModelLoader.setCustomModelResourceLocation(Main.proxy.itemBlockCable, DEFAULT_ITEM_SUBTYPE,
                new ModelResourceLocation("theonecable:cable", "inventory"));




        // Register our ModelLoader
        ModelLoaderRegistry.registerLoader(new MyModelLoader());
    }

    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        //System.out.println(I18n.format("tile.cable.name"));
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

}
