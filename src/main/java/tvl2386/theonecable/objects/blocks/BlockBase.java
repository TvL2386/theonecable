package tvl2386.theonecable.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import tvl2386.theonecable.Main;
import tvl2386.theonecable.init.BlockInit;
import tvl2386.theonecable.init.ItemInit;
import tvl2386.theonecable.util.IHasModel;

//06 16:37:29 < TvL2386> hey guys, I'm trying to put blocks into the world, but I keep seeing the black/purple shape and errors are being thrown when MC is starting
//06 16:38:00 < TvL2386> I know that based on the names files are loaded in the resources folder, but I can't find the exact docs that show how to do it
//06 16:39:25 < ghz|afk> TvL2386: if your block's registry name is "a:b", mc will look in assets/a/blockstates/b.json
//06 16:39:50 < ghz|afk> "normal" is the default variant entry if no properties are present
//06 16:40:09 < ghz|afk> otherwise "property1=value1,property2=value2,..." with the properties in alphabetical order

public class BlockBase extends Block implements IHasModel {

    public BlockBase(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MISC);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
