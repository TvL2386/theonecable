package tvl2386.theonecable.objects.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import tvl2386.theonecable.Main;

//public class CableTileEntity extends TileEntity implements ITickable, IEnergyReceiver, IEnergyProvider, IEnergyStorage {
public class CableTileEntity extends TileEntity implements ITickable, IEnergyStorage {

    private int storedEnergy;
    private int maxStoredEnergy;

    public CableTileEntity(int storedEnergy, int maxStoredEnergy)
    {
        this.storedEnergy = storedEnergy;
        this.maxStoredEnergy = maxStoredEnergy;
    }

    public CableTileEntity()
    {
        new CableTileEntity(0, 10000);
    }

    // ITickable methods
    @Override
    public void update() {
        //Main.logger.info("stored: "+this.storedEnergy+" max: "+this.maxStoredEnergy);
    }

//    // IEnergyReceiver methods
//    /**
//     * Adds energy to the storage. Returns quantity of energy that was accepted.
//     *
//     * @param maxReceive
//     *            Maximum amount of energy to be inserted.
//     * @param simulate
//     *            If TRUE, the insertion will only be simulated.
//     * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
//     */
//    @Override
//    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
//        return receiveEnergy(maxReceive, simulate);
//    }
//
//    /**
//     * Returns the amount of energy currently stored.
//     */
//    @Override
//    public int getEnergyStored(EnumFacing from) {
//        return this.storedEnergy;
//    }
//
//    /**
//     * Returns the maximum amount of energy that can be stored.
//     */
//    @Override
//    public int getMaxEnergyStored(EnumFacing from) {
//        return this.maxStoredEnergy;
//    }
//
//    @Override
//    public boolean canConnectEnergy(EnumFacing from) {
//        return true;
//    }
//
//    // IEnergyProvider methods
//    @Override
//    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
//        return extractEnergy(maxExtract, simulate);
//    }

    // IEnergyStorage methods
    /**
     * Adds energy to the storage. Returns quantity of energy that was accepted.
     *
     * @param maxReceive Maximum amount of energy to be inserted.
     * @param simulate   If TRUE, the insertion will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
     */
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        //Main.logger.info("receiveEnergy(maxReceive: "+maxReceive+" simulate: "+simulate+");");
        int canReceive = this.maxStoredEnergy - this.storedEnergy;

        if(canReceive > maxReceive)
            canReceive = maxReceive;

        if(!simulate)
            this.storedEnergy += canReceive;

        return canReceive;
    }

    /**
     * Removes energy from the storage. Returns quantity of energy that was removed.
     *
     * @param maxExtract Maximum amount of energy to be extracted.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        //Main.logger.info("extractEnergy(maxExtract: "+maxExtract+" simulate: "+simulate+");");
        int canExtract = this.storedEnergy;

        if(canExtract > maxExtract)
            canExtract = maxExtract;

        if(!simulate)
            this.storedEnergy -= canExtract;

        return canExtract;
    }

    /**
     * Returns the amount of energy currently stored.
     */
    @Override
    public int getEnergyStored() {
        //Main.logger.info("getEnergyStored();");
        return this.storedEnergy;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    @Override
    public int getMaxEnergyStored() {
        //Main.logger.info("getMaxEnergyStored();");
        return this.maxStoredEnergy;
    }

    /**
     * Returns if this storage can have energy extracted.
     * If this is false, then any calls to extractEnergy will return 0.
     */
    @Override
    public boolean canExtract() {
        //Main.logger.info("canExtract();");
        return true;
    }

    /**
     * Used to determine if this storage can receive energy.
     * If this is false, then any calls to receiveEnergy will return 0.
     */
    @Override
    public boolean canReceive() {
        //Main.logger.info("canReceive();");
        return true;
    }

    // Forge energy
    private IEnergyStorage[] sidedHandlers = new IEnergyStorage[6];
    private IEnergyStorage nullHandler;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            if (facing == null) {
                if (nullHandler == null) {
                    createNullHandler();
                }
                return (T) nullHandler;
            } else {
                if (sidedHandlers[facing.ordinal()] == null) {
                    createSidedHandler(facing);
                }
                return (T) sidedHandlers[facing.ordinal()];
            }
        }
        return super.getCapability(capability, facing);
    }

    private void createSidedHandler(EnumFacing facing) {
        Main.logger.info("createSidedHandler for "+facing.getName());

        sidedHandlers[facing.ordinal()] = new IEnergyStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return CableTileEntity.this.receiveEnergyFacing(facing, maxReceive, simulate);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return CableTileEntity.this.extractEnergy(maxExtract, simulate);
            }

            @Override
            public int getEnergyStored() {
                return CableTileEntity.this.getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return CableTileEntity.this.getMaxEnergyStored();
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public boolean canReceive() {
                return true;
            }
        };
    }

    private int receiveEnergyFacing(EnumFacing facing, int maxReceive, boolean simulate) {
        Main.logger.info("receiving energy from "+facing.getName()+": maxReceive: "+maxReceive+" simulate: "+simulate);
        return receiveEnergy(maxReceive, simulate);
    }

    private void createNullHandler() {
        nullHandler = new IEnergyStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return 0;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return 0;
            }

            @Override
            public int getEnergyStored() {
                return CableTileEntity.this.getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return CableTileEntity.this.getMaxEnergyStored();
            }

            @Override
            public boolean canExtract() {
                return false;
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        };
    }
//    private final int INVALID_VALUE = -1;
//    private int ticksLeftTillDisappear = INVALID_VALUE;  // the time (in ticks) left until the block disappears
//
//    // set by the block upon creation
//    public void setTicksLeftTillDisappear(int ticks)
//    {
//        ticksLeftTillDisappear = ticks;
//    }
//
//    // When the world loads from disk, the server needs to send the TileEntity information to the client
//    //  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
//    //  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
//    //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
//    //  Not really required for this example since we only use the timer on the client, but included anyway for illustration
//    @Override
//    @Nullable
//    public SPacketUpdateTileEntity getUpdatePacket()
//    {
//        NBTTagCompound nbtTagCompound = new NBTTagCompound();
//        writeToNBT(nbtTagCompound);
//        int metadata = getBlockMetadata();
//        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
//        readFromNBT(pkt.getNbtCompound());
//    }
//
//    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
//     */
//    @Override
//    public NBTTagCompound getUpdateTag()
//    {
//        NBTTagCompound nbtTagCompound = new NBTTagCompound();
//        writeToNBT(nbtTagCompound);
//        return nbtTagCompound;
//    }
//
//    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
//     */
//    @Override
//    public void handleUpdateTag(NBTTagCompound tag)
//    {
//        this.readFromNBT(tag);
//    }
//
//    // This is where you save any data that you don't want to lose when the tile entity unloads
//    // In this case, we only need to store the ticks left until explosion, but we store a bunch of other
//    //  data as well to serve as an example.
//    // NBTexplorer is a very useful tool to examine the structure of your NBT saved data and make sure it's correct:
//    //   http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac
//    @Override
//    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
//    {
//        super.writeToNBT(parentNBTTagCompound); // The super call is required to save the tiles location
//
//        parentNBTTagCompound.setInteger("ticksLeft", ticksLeftTillDisappear);
//        // alternatively - could use parentNBTTagCompound.setTag("ticksLeft", new NBTTagInt(ticksLeftTillDisappear));
//
//        // some examples of other NBT tags - browse NBTTagCompound or search for the subclasses of NBTBase for more examples
//
//        parentNBTTagCompound.setString("testString", testString);
//
//        NBTTagCompound blockPosNBT = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
//        blockPosNBT.setInteger("x", testBlockPos.getX());
//        blockPosNBT.setInteger("y", testBlockPos.getY());
//        blockPosNBT.setInteger("z", testBlockPos.getZ());
//        parentNBTTagCompound.setTag("testBlockPos", blockPosNBT);
//
//        NBTTagCompound itemStackNBT = new NBTTagCompound();
//        testItemStack.writeToNBT(itemStackNBT);                     // make sure testItemStack is not null first!
//        parentNBTTagCompound.setTag("testItemStack", itemStackNBT);
//
//        parentNBTTagCompound.setIntArray("testIntArray", testIntArray);
//
//        NBTTagList doubleArrayNBT = new NBTTagList();                     // an NBTTagList is similar to a Java ArrayList
//        for (double value : testDoubleArray) {
//            doubleArrayNBT.appendTag(new NBTTagDouble(value));
//        }
//        parentNBTTagCompound.setTag("testDoubleArray", doubleArrayNBT);
//
//        NBTTagList doubleArrayWithNullsNBT = new NBTTagList();
//        for (int i = 0; i < testDoubleArrayWithNulls.length; ++i) {
//            Double value = testDoubleArrayWithNulls[i];
//            if (value != null) {
//                NBTTagCompound dataForThisSlot = new NBTTagCompound();
//                dataForThisSlot.setInteger("i", i+1);   // avoid using 0, so the default when reading a missing value (0) is obviously invalid
//                dataForThisSlot.setDouble("v", value);
//                doubleArrayWithNullsNBT.appendTag(dataForThisSlot);
//            }
//        }
//        parentNBTTagCompound.setTag("testDoubleArrayWithNulls", doubleArrayWithNullsNBT);
//        return parentNBTTagCompound;
//    }
//
//    // This is where you load the data that you saved in writeToNBT
//    @Override
//    public void readFromNBT(NBTTagCompound parentNBTTagCompound)
//    {
//        super.readFromNBT(parentNBTTagCompound); // The super call is required to load the tiles location
//
//        // important rule: never trust the data you read from NBT, make sure it can't cause a crash
//
//        final int NBT_INT_ID = 3;					// see NBTBase.createNewByType()
//        int readTicks = INVALID_VALUE;
//        if (parentNBTTagCompound.hasKey("ticksLeft", NBT_INT_ID)) {  // check if the key exists and is an Int. You can omit this if a default value of 0 is ok.
//            readTicks = parentNBTTagCompound.getInteger("ticksLeft");
//            if (readTicks < 0) readTicks = INVALID_VALUE;
//        }
//        ticksLeftTillDisappear = readTicks;
//
//        // some examples of other NBT tags - browse NBTTagCompound or search for the subclasses of NBTBase for more
//
//        String readTestString = null;
//        final int NBT_STRING_ID = 8;          // see NBTBase.createNewByType()
//        if (parentNBTTagCompound.hasKey("testString", NBT_STRING_ID)) {
//            readTestString = parentNBTTagCompound.getString("testString");
//        }
//        if (!testString.equals(readTestString)) {
//            System.err.println("testString mismatch:" + readTestString);
//        }
//
//        NBTTagCompound blockPosNBT = parentNBTTagCompound.getCompoundTag("testBlockPos");
//        BlockPos readBlockPos = null;
//        if (blockPosNBT.hasKey("x", NBT_INT_ID) && blockPosNBT.hasKey("y", NBT_INT_ID) && blockPosNBT.hasKey("z", NBT_INT_ID) ) {
//            readBlockPos = new BlockPos(blockPosNBT.getInteger("x"), blockPosNBT.getInteger("y"), blockPosNBT.getInteger("z"));
//        }
//        if (readBlockPos == null || !testBlockPos.equals(readBlockPos)) {
//            System.err.println("testBlockPos mismatch:" + readBlockPos);
//        }
//
//        NBTTagCompound itemStackNBT = parentNBTTagCompound.getCompoundTag("testItemStack");
//        ItemStack readItemStack = new ItemStack(itemStackNBT);
//        if (!ItemStack.areItemStacksEqual(testItemStack, readItemStack)) {
//            System.err.println("testItemStack mismatch:" + readItemStack);
//        }
//
//        int [] readIntArray = parentNBTTagCompound.getIntArray("testIntArray");
//        if (!Arrays.equals(testIntArray, readIntArray)) {
//            System.err.println("testIntArray mismatch:" + readIntArray);
//        }
//
//        final int NBT_DOUBLE_ID = 6;					// see NBTBase.createNewByType()
//        NBTTagList doubleArrayNBT = parentNBTTagCompound.getTagList("testDoubleArray", NBT_DOUBLE_ID);
//        int numberOfEntries = Math.min(doubleArrayNBT.tagCount(), testDoubleArray.length);
//        double [] readDoubleArray = new double[numberOfEntries];
//        for (int i = 0; i < numberOfEntries; ++i) {
//            readDoubleArray[i] = doubleArrayNBT.getDoubleAt(i);
//        }
//        if (doubleArrayNBT.tagCount() != numberOfEntries || !Arrays.equals(readDoubleArray, testDoubleArray)) {
//            System.err.println("testDoubleArray mismatch:" + readDoubleArray);
//        }
//
//        final int NBT_COMPOUND_ID = 10;					// see NBTBase.createNewByType()
//        NBTTagList doubleNullArrayNBT = parentNBTTagCompound.getTagList("testDoubleArrayWithNulls", NBT_COMPOUND_ID);
//        numberOfEntries = Math.min(doubleArrayNBT.tagCount(), testDoubleArrayWithNulls.length);
//        Double [] readDoubleNullArray = new Double[numberOfEntries];
//        for (int i = 0; i < doubleNullArrayNBT.tagCount(); ++i)	{
//            NBTTagCompound nbtEntry = doubleNullArrayNBT.getCompoundTagAt(i);
//            int idx = nbtEntry.getInteger("i") - 1;
//            if (nbtEntry.hasKey("v", NBT_DOUBLE_ID) && idx >= 0 && idx < numberOfEntries) {
//                readDoubleNullArray[idx] = nbtEntry.getDouble("v");
//            }
//        }
//        if (!Arrays.equals(testDoubleArrayWithNulls, readDoubleNullArray)) {
//            System.err.println("testDoubleArrayWithNulls mismatch:" + readDoubleNullArray);
//        }
//    }
//
//    // Since our TileEntity implements ITickable, we get an update method which is called once per tick (20 times / second)
//    // When the timer elapses, replace our block with a random one.
//    @Override
//    public void update() {
//        if (!this.hasWorld()) return;  // prevent crash
//        World world = this.getWorld();
//        if (world.isRemote) return;   // don't bother doing anything on the client side.
//        if (ticksLeftTillDisappear == INVALID_VALUE) return;  // do nothing until the time is valid
//        --ticksLeftTillDisappear;
////		this.markDirty();            // if you update a tileentity variable on the server and this should be communicated to the client,
//// 																		you need to markDirty() to force a resend.  In this case, the client doesn't need to know
//        if (ticksLeftTillDisappear > 0) return;   // not ready yet
//
//        Block[] blockChoices = {Blocks.DIAMOND_BLOCK, Blocks.OBSIDIAN, Blocks.AIR, Blocks.TNT, Blocks.YELLOW_FLOWER, Blocks.SAPLING, Blocks.WATER};
//        Random random = new Random();
//        Block chosenBlock = blockChoices[random.nextInt(blockChoices.length)];
//        world.setBlockState(this.pos, chosenBlock.getDefaultState());
//        if (chosenBlock == Blocks.TNT) {
//            Blocks.TNT.onBlockDestroyedByPlayer(world, pos, Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
//            world.setBlockToAir(pos);
//        } else if (chosenBlock == Blocks.SAPLING) {
//            BlockSapling blockSapling = (BlockSapling)Blocks.SAPLING;
//            blockSapling.generateTree(world, this.pos, blockSapling.getDefaultState(),random);
//        }
//    }
//
//    private final int [] testIntArray = {5, 4, 3, 2, 1};
//    private final double [] testDoubleArray = {1, 2, 3, 4, 5, 6};
//    private final Double [] testDoubleArrayWithNulls = {61.1, 62.2, null, 64.4, 65.5};
//    private final ItemStack testItemStack = new ItemStack(Items.COOKED_CHICKEN, 23);
//    private final String testString = "supermouse";
//    private final BlockPos testBlockPos = new BlockPos(10, 11, 12);

}
