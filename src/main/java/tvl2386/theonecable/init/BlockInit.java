package tvl2386.theonecable.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import tvl2386.theonecable.objects.blocks.BlockBase;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {

    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block BLOCK_CABLE = new BlockBase("cable", Material.IRON);

}
