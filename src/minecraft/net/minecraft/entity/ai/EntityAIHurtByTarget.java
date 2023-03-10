package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget
{
    boolean entityCallsForHelp;
    private int revengeTimerOld;
    private static final String __OBFID = "CL_00001619";

    public EntityAIHurtByTarget(EntityCreature p_i1660_1_, boolean p_i1660_2_)
    {
        super(p_i1660_1_, false);
        this.entityCallsForHelp = p_i1660_2_;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        int var1 = this.taskOwner.getRevengeTimer();
        return var1 != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();

        if (this.entityCallsForHelp)
        {
            double var1 = this.getTargetDistance();
            List var3 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), AxisAlignedBB.getBoundingBox(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).expand(var1, 10.0D, var1));
            Iterator var4 = var3.iterator();

            while (var4.hasNext())
            {
                EntityCreature var5 = (EntityCreature)var4.next();

                if (this.taskOwner != var5 && var5.getAttackTarget() == null && !var5.isOnSameTeam(this.taskOwner.getAITarget()))
                {
                    var5.setAttackTarget(this.taskOwner.getAITarget());
                }
            }
        }

        super.startExecuting();
    }
}
