package tvl2386.theonecable.proxy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tvl2386.theonecable.objects.blocks.Block3DWeb;
import tvl2386.theonecable.objects.blocks.BlockCable;
import tvl2386.theonecable.objects.blocks.CableTileEntity;

public class CommonProxy {

    public static Block3DWeb block3DWeb;
    public static ItemBlock itemBlock3DWeb;

    public static BlockCable blockCable;
    public static ItemBlock itemBlockCable;

    public void preInit(FMLPreInitializationEvent event)
    {
        // each instance of your block should have two names:
        // 1) a registry name that is used to uniquely identify this block.  Should be unique within your mod.  use lower case.
        // 2) an 'unlocalised name' that is used to retrieve the text name of your block in the player's language.  For example-
        //    the unlocalised name might be "water", which is printed on the user's screen as "Wasser" in German or
        //    "aqua" in Italian.
        //
        //    Multiple blocks can have the same unlocalised name - for example
        //  +----RegistryName----+---UnlocalisedName----+
        //  |  flowing_water     +       water          |
        //  |  stationary_water  +       water          |
        //  +--------------------+----------------------+
        //

        block3DWeb = new Block3DWeb();
        block3DWeb.setUnlocalizedName("mbe05_block_3d_web_unlocalised_name");
        block3DWeb.setRegistryName("mbe05_block_3d_web_registry_name");
        ForgeRegistries.BLOCKS.register(block3DWeb);

        itemBlock3DWeb = new ItemBlock(block3DWeb);
        itemBlock3DWeb.setRegistryName(block3DWeb.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock3DWeb);

        blockCable = new BlockCable();
        blockCable.setUnlocalizedName("cable");
        blockCable.setRegistryName("cable");
        ForgeRegistries.BLOCKS.register(blockCable);

        itemBlockCable = new ItemBlock(blockCable);
        itemBlockCable.setRegistryName(blockCable.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlockCable);

        // Each of your tile entities needs to be registered with a name that is unique to your mod.
        GameRegistry.registerTileEntity(CableTileEntity.class, "cable_tile_entity");
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void registerItemRenderer(Item item, int meta, String id)
    {

    }
}
