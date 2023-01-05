package net.minecraft.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase
{
    private IIcon field_150056_b;
    private static final String __OBFID = "CL_00000293";

    protected BlockRail()
    {
        super(false);
    }

    public IIcon getIcon(int side, int meta)
    {
        return meta >= 6 ? this.field_150056_b : this.blockIcon;
    }

    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
        this.field_150056_b = reg.registerIcon(this.getTextureName() + "_turned");
    }

    protected void onRedstoneSignal(World p_150048_1_, int p_150048_2_, int p_150048_3_, int p_150048_4_, int p_150048_5_, int p_150048_6_, Block p_150048_7_)
    {
        if (p_150048_7_.canProvidePower() && (new BlockRailBase.Rail(p_150048_1_, p_150048_2_, p_150048_3_, p_150048_4_)).countAdjacentRails() == 3)
        {
            this.refreshTrackShape(p_150048_1_, p_150048_2_, p_150048_3_, p_150048_4_, false);
        }
    }
}
