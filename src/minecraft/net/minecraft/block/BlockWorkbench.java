package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWorkbench extends Block
{
    private IIcon field_150035_a;
    private IIcon field_150034_b;
    private static final String __OBFID = "CL_00000221";

    protected BlockWorkbench()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_150035_a : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? this.blockIcon : this.field_150034_b));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.field_150035_a = reg.registerIcon(this.getTextureName() + "_top");
        this.field_150034_b = reg.registerIcon(this.getTextureName() + "_front");
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            player.displayGUIWorkbench(x, y, z);
            return true;
        }
    }
}
