package tvl2386.theonecable.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCable extends Block {

    public BlockCable()
    {
        super(Material.WEB);
        setCreativeTab(CreativeTabs.MISC);
    }

    // the block will render in the SOLID layer.
    // See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    // used by the renderer to control lighting and visibility of other blocks.
    // set to false because this block doesn't fully occupy the entire 1x1x1 space
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

//    // make colliding players stick in the web like normal web
//    @Override
//    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
//    {
//        entityIn.setInWeb();
//    }

    // used by the renderer to control lighting and visibility of other blocks, also by
    // (eg) wall or fence to control whether the fence joins itself to this block
    // set to false because this block does not occupy the entire 1x1x1 space
    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    // render using an IBakedModel
    // not strictly required because the default (super method) is MODEL.
    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }

    // by returning a null collision bounding box we stop the player from colliding with it
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    // createBlockState is used to define which properties your blocks possess
    // Vanilla BlockState is composed of listed properties only.  A variant is created for each combination of listed
    //   properties; for example two properties ON(true/false) and READY(true/false) would give rise to four variants
    //   [on=true, ready=true]
    //   [on=false, ready=true]
    //   [on=true, ready=false]
    //   [on=false, ready=false]
    // Forge adds ExtendedBlockState, which has two types of property:
    // - listed properties (like vanilla), and
    // - unlisted properties, which can be used to convey information but do not cause extra variants to be created.
    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[0]; // no listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] {LINK_UP, LINK_DOWN, LINK_EAST, LINK_WEST, LINK_NORTH, LINK_SOUTH};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    // this method uses the block state and BlockPos to update the unlisted LINK properties in IExtendedBlockState based
    // on non-metadata information.  This is then conveyed to the IBakedModel#getQuads during rendering.
    // In this case, we look around the block to see which faces are next to either a solid block or another web block:
    // The web node forms a strand of web to any adjacent solid blocks or web nodes
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {  // avoid crash in case of mismatch
            IExtendedBlockState retval = (IExtendedBlockState)state;
            boolean linkup = canConnectTo(world, pos.up());
            retval = retval.withProperty(LINK_UP, linkup);

            boolean linkdown = canConnectTo(world, pos.down());
            retval = retval.withProperty(LINK_DOWN, linkdown);

            boolean linkeast = canConnectTo(world, pos.east());
            retval = retval.withProperty(LINK_EAST, linkeast);

            boolean linkwest = canConnectTo(world, pos.west());
            retval = retval.withProperty(LINK_WEST, linkwest);

            boolean linknorth = canConnectTo(world, pos.north());
            retval = retval.withProperty(LINK_NORTH, linknorth);

            boolean linksouth = canConnectTo(world, pos.south());
            retval = retval.withProperty(LINK_SOUTH, linksouth);

            return retval;
        }
        return state;
    }

    private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (block == Blocks.BARRIER) return false;
        if (block == this) return true;
        return false;
    }

    // the LINK properties are used to communicate to the ISmartBlockModel which of the links should be drawn
    public static final IUnlistedProperty<Boolean> LINK_UP = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("link_up"));
    public static final IUnlistedProperty<Boolean> LINK_DOWN = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("link_down"));
    public static final IUnlistedProperty<Boolean> LINK_WEST = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("link_west"));
    public static final IUnlistedProperty<Boolean> LINK_EAST = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("link_east"));
    public static final IUnlistedProperty<Boolean> LINK_NORTH = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("link_north"));
    public static final IUnlistedProperty<Boolean> LINK_SOUTH = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("link_south"));

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    // Called when the block is placed or loaded client side to get the tile entity for the block
    // Should return a new instance of the tile entity for the block
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new CableTileEntity();
    }

    private final int TIMER_COUNTDOWN_TICKS = 20 * 10; // duration of the countdown, in ticks = 10 seconds

    // Called just after the player places a block.  Start the tileEntity's timer
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CableTileEntity) { // prevent a crash if not the right type, or is null
            CableTileEntity tileEntityData = (CableTileEntity)tileEntity;
            tileEntityData.setTicksLeftTillDisappear(TIMER_COUNTDOWN_TICKS);
        }
    }



}
