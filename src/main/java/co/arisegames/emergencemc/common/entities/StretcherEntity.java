package co.arisegames.emergencemc.common.entities;

import co.arisegames.emergencemc.EmergenceMC;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.entity.item.BoatEntity.func_242378_a;

public class StretcherEntity extends Entity {
    private final Map<PlayerEntity, Vector2f> movementVecMap = new HashMap<>();
    private final Map<PlayerEntity, Vector3d> offsetsMap = new HashMap<>();
    private final float defaultDistance = 1.2f;
    private float playerDistance = 0;


    public StretcherEntity(EntityType<StretcherEntity> type, World world) {
        super(type, world);
        this.stepHeight = 0.5f;
    }


    @Override
    protected void registerData() {
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public void tick() {
        super.tick();
        this.updateMotion();

//        if (this.world.isRemote) {
//            this.controlStretcher();
//        }

        this.move(MoverType.SELF, this.getMotion());

        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow((double) 0.2F, (double) -0.01F, (double) 0.2F), EntityPredicates.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof PlayerEntity);

            for (Entity entity : list) {
                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isPassenger() && entity.getWidth() < this.getWidth() && entity instanceof LivingEntity && !(entity instanceof WaterMobEntity) && !(entity instanceof PlayerEntity)) {
                        entity.startRiding(this);
                    } else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }
    }

    private void controlStretcher() {
        this.setMotion(this.getMotion().add(Vector3d.ZERO));
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        this.removePassengers();
        return true;
    }

    public int getPlayerPassengers() {
        return (int) this.getPassengers().stream().filter(e -> e instanceof PlayerEntity).count();
    }

    private int getNonPlayerPassengers() {
        return (int) this.getPassengers().stream().filter(e -> !(e instanceof PlayerEntity)).count();
    }

    private float getSpeed() {
        if (getNonPlayerPassengers() == 0) {
            return 0.12f;
        }
        switch (getPlayerPassengers()) {
            case 0:
                return 0;
            case 1:
                return 0.03f;
            default:
                return 0.12f;
        }
    }

    private void updateMotion() {
        Vector3d motion = this.movementVecMap.values().stream()
                .map(m -> new Vector3d(m.x, 0, m.y))
                .reduce(Vector3d.ZERO, Vector3d::add).scale(getSpeed() * getSpeedFactor());
        Vector3d gravity = new Vector3d(0, -1, 0).scale(1);
        this.setMotion(motion.add(gravity));
        if (motion.dotProduct(motion) > 0) {
            this.prevRotationYaw = this.rotationYaw;
            float newRotationYaw = (float) (Math.atan2(motion.z, motion.x) * 180 / Math.PI - 90);
//            this.rotationYaw = (float) MathHelper.lerp(0.5, this.prevRotationYaw, newRotationYaw);
            this.rotationYaw = MathHelper.approachDegrees(prevRotationYaw, newRotationYaw, 30);
        }
    }

    /**
     * Applies this boat's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate) {
        float offset = 0;
        if (getPassengerIndex(entityToUpdate) == 1) {
            offset = 180;
        }
        float yaw = this.rotationYaw + offset;
        entityToUpdate.setRenderYawOffset(yaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - yaw);
//        float f1 = f;
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    private int getPassengerIndex(Entity passenger) {
        return this.getPassengers().indexOf(passenger);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        double d0 = this.getPosY() + this.getMountedYOffset() + passenger.getYOffset();
        Vector3d pos = new Vector3d(this.getPosX(), d0, this.getPosZ());
        if (passenger instanceof PlayerEntity) {
            int index = getPassengerIndex(passenger);
            Vector3d offset = new Vector3d(0, 0, -defaultDistance).rotateYaw((float) (-rotationYaw * Math.PI / 180));
            if (index == 1) offset = offset.rotateYaw((float) Math.PI);
//            passenger.setRotationYawHe ad(index == 0 ? this.rotationYaw : this.rotationYaw + 180);
            pos = pos.add(offset);
        } else {
            double passengerHeightOffset = getPlayerPassengers() == 0 ? 0.65 : 0.1;
            pos = pos.add(0, passengerHeightOffset, 0);
        }
        applyYawToEntity(passenger);
        passenger.setPosition(pos.x, pos.y, pos.z);
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
    }

    public boolean canCollide(Entity entity) {
        return func_242378_a(this, entity);
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        super.applyEntityCollision(entityIn);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return true;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger)  {
        if (passenger instanceof PlayerEntity) {
            return getPlayerPassengers() < 2;
        }
        return getNonPlayerPassengers() < 1;    }

    @Override
    public Entity getControllingPassenger() {
        return super.getControllingPassenger();
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (!(passenger instanceof PlayerEntity))
            passenger.setPose(Pose.DYING);
        else
            passenger.setPose(Pose.STANDING);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof PlayerEntity) {
            this.movementVecMap.remove(passenger);
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
    }

    public void updateInputs(PlayerEntity player, Vector2f vec) {
        this.movementVecMap.put(player, vec);
    }

    @Override
    public double getMountedYOffset() {
        return getPlayerPassengers() == 0 ? -9/16f : 6/16f;
    }

    @Override
    public double getYOffset() {
        return 0.5;
    }
}
