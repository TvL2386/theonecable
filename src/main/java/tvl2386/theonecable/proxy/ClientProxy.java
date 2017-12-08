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
import tvl2386.theonecable.objects.blocks.ModelLoader3DWeb;
import tvl2386.theonecable.objects.blocks.ModelLoaderCable;

public class ClientProxy extends CommonProxy {

    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        // We need to tell Forge how to map our Block3DWebs's IBlockState to a ModelResourceLocation.
        // For example, the BlockStone granite variant has a BlockStateMap entry that looks like
        //   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
        // For the 3DWeb block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
        //   which is done using the anonymous class below
        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation("theonecable:mbe05_block_3d_web_statemapper_name");
            }
        };
        ModelLoader.setCustomStateMapper(Main.proxy.block3DWeb, ignoreState);

        ModelLoaderRegistry.registerLoader(new ModelLoader3DWeb());

        // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
        //   or in your hand or thrown on the ground).
        // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
        //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
        //   of any extra items you have created.  Hence you have to do it manually.
        // It must be done on client only, and must be done after the block has been created in Common.preinit().

        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("theonecable:mbe05_block_3d_web", "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(Main.proxy.itemBlock3DWeb, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);









        ModelLoader.setCustomStateMapper(Main.proxy.blockCable, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation("theonecable:cable_statemapper_name");
            }
        });

        ModelLoaderRegistry.registerLoader(new ModelLoaderCable());

        ModelLoader.setCustomModelResourceLocation(Main.proxy.itemBlockCable, 0, new ModelResourceLocation("theonecable:cable", "inventory"));

//        StateMapperBase ignoreState = new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
//                return new ModelResourceLocation("theonecable:cable_statemapper_name");
//            }
//        };
//        ModelLoader.setCustomStateMapper(Main.proxy.blockCable, ignoreState);
//
//        ModelLoaderRegistry.registerLoader(new ModelLoaderCable());
//
//
//        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("theonecable:cable", "inventory");
//        final int DEFAULT_ITEM_SUBTYPE = 0;
//        ModelLoader.setCustomModelResourceLocation(Main.proxy.itemBlockCable, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
//
    }

    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        //super.registerItemRenderer(item, meta, id);
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

}
