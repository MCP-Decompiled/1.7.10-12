package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class BlockHay extends BlockRotatedPillar
{
    private static final String __OBFID = "CL_00000256";

    public BlockHay()
    {
        super(Material.grass);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    protected IIcon getSideIcon(int p_150163_1_)
    {
        return this.blockIcon;
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150164_N = reg.registerIcon(this.getTextureName() + "_top");
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
    }
}
