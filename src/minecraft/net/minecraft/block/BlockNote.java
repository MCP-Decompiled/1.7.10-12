package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer
{
    private static final String __OBFID = "CL_00000278";

    public BlockNote()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        boolean var6 = worldIn.isBlockIndirectlyGettingPowered(x, y, z);
        TileEntityNote var7 = (TileEntityNote)worldIn.getTileEntity(x, y, z);

        if (var7 != null && var7.previousRedstoneState != var6)
        {
            if (var6)
            {
                var7.triggerNote(worldIn, x, y, z);
            }

            var7.previousRedstoneState = var6;
        }
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityNote var10 = (TileEntityNote)worldIn.getTileEntity(x, y, z);

            if (var10 != null)
            {
                var10.changePitch();
                var10.triggerNote(worldIn, x, y, z);
            }

            return true;
        }
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player)
    {
        if (!worldIn.isRemote)
        {
            TileEntityNote var6 = (TileEntityNote)worldIn.getTileEntity(x, y, z);

            if (var6 != null)
            {
                var6.triggerNote(worldIn, x, y, z);
            }
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityNote();
    }

    public boolean onBlockEventReceived(World worldIn, int x, int y, int z, int eventId, int eventData)
    {
        float var7 = (float)Math.pow(2.0D, (double)(eventData - 12) / 12.0D);
        String var8 = "harp";

        if (eventId == 1)
        {
            var8 = "bd";
        }

        if (eventId == 2)
        {
            var8 = "snare";
        }

        if (eventId == 3)
        {
            var8 = "hat";
        }

        if (eventId == 4)
        {
            var8 = "bassattack";
        }

        worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "note." + var8, 3.0F, var7);
        worldIn.spawnParticle("note", (double)x + 0.5D, (double)y + 1.2D, (double)z + 0.5D, (double)eventData / 24.0D, 0.0D, 0.0D);
        return true;
    }
}
