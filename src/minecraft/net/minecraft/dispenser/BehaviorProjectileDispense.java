package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem
{
    private static final String __OBFID = "CL_00001394";

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
        World var3 = source.getWorld();
        IPosition var4 = BlockDispenser.getIPositionFromBlockSource(source);
        EnumFacing var5 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
        IProjectile var6 = this.getProjectileEntity(var3, var4);
        var6.setThrowableHeading((double)var5.getFrontOffsetX(), (double)((float)var5.getFrontOffsetY() + 0.1F), (double)var5.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        var3.spawnEntityInWorld((Entity)var6);
        stack.splitStack(1);
        return stack;
    }

    protected void playDispenseSound(IBlockSource source)
    {
        source.getWorld().playAuxSFX(1002, source.getXInt(), source.getYInt(), source.getZInt(), 0);
    }

    protected abstract IProjectile getProjectileEntity(World worldIn, IPosition position);

    protected float func_82498_a()
    {
        return 6.0F;
    }

    protected float func_82500_b()
    {
        return 1.1F;
    }
}
