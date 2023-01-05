package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBoat extends Item
{
    private static final String __OBFID = "CL_00001774";

    public ItemBoat()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        float var4 = 1.0F;
        float var5 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * var4;
        float var6 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * var4;
        double var7 = player.prevPosX + (player.posX - player.prevPosX) * (double)var4;
        double var9 = player.prevPosY + (player.posY - player.prevPosY) * (double)var4 + 1.62D - (double)player.yOffset;
        double var11 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)var4;
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
        MovingObjectPosition var24 = worldIn.rayTraceBlocks(var13, var23, true);

        if (var24 == null)
        {
            return itemStackIn;
        }
        else
        {
            Vec3 var25 = player.getLook(var4);
            boolean var26 = false;
            float var27 = 1.0F;
            List var28 = worldIn.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(var25.xCoord * var21, var25.yCoord * var21, var25.zCoord * var21).expand((double)var27, (double)var27, (double)var27));
            int var29;

            for (var29 = 0; var29 < var28.size(); ++var29)
            {
                Entity var30 = (Entity)var28.get(var29);

                if (var30.canBeCollidedWith())
                {
                    float var31 = var30.getCollisionBorderSize();
                    AxisAlignedBB var32 = var30.boundingBox.expand((double)var31, (double)var31, (double)var31);

                    if (var32.isVecInside(var13))
                    {
                        var26 = true;
                    }
                }
            }

            if (var26)
            {
                return itemStackIn;
            }
            else
            {
                if (var24.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    var29 = var24.blockX;
                    int var33 = var24.blockY;
                    int var34 = var24.blockZ;

                    if (worldIn.getBlock(var29, var33, var34) == Blocks.snow_layer)
                    {
                        --var33;
                    }

                    EntityBoat var35 = new EntityBoat(worldIn, (double)((float)var29 + 0.5F), (double)((float)var33 + 1.0F), (double)((float)var34 + 0.5F));
                    var35.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                    if (!worldIn.getCollidingBoundingBoxes(var35, var35.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return itemStackIn;
                    }

                    if (!worldIn.isRemote)
                    {
                        worldIn.spawnEntityInWorld(var35);
                    }

                    if (!player.capabilities.isCreativeMode)
                    {
                        --itemStackIn.stackSize;
                    }
                }

                return itemStackIn;
            }
        }
    }
}
