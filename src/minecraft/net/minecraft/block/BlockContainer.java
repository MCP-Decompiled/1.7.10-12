package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider
{
    private static final String __OBFID = "CL_00000193";

    protected BlockContainer(Material p_i45386_1_)
    {
        super(p_i45386_1_);
        this.isBlockContainer = true;
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
        worldIn.removeTileEntity(x, y, z);
    }

    public boolean onBlockEventReceived(World worldIn, int x, int y, int z, int eventId, int eventData)
    {
        super.onBlockEventReceived(worldIn, x, y, z, eventId, eventData);
        TileEntity var7 = worldIn.getTileEntity(x, y, z);
        return var7 != null ? var7.receiveClientEvent(eventId, eventData) : false;
    }
}
