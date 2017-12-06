package tvl2386.theonecable.objects.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tvl2386.theonecable.Main;
import tvl2386.theonecable.init.ItemInit;
import tvl2386.theonecable.proxy.ClientProxy;
import tvl2386.theonecable.util.IHasModel;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
