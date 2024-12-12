package cc.xypp.battery_shield.helper;

import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.items.Register;
import cc.xypp.battery_shield.packet.TrackingPackat;
import cc.xypp.battery_shield.utils.EntityUtil;
import cc.xypp.battery_shield.utils.TypeBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class TrackingManager {
    public static TrackingManager instance;

    public static TrackingManager getInstance() {
        return instance != null ? instance : (instance = new TrackingManager());
    }

    public final SimpleChannel INSTANCE;

    public TrackingManager() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("shield_tracking", "shield_tracking"),
                () -> "1.0.0",
                (v) -> v.equals("1.0.0"),
                (v) -> v.equals("1.0.0")
        );
        INSTANCE.registerMessage(0, TrackingPackat.class, TrackingPackat::encode, TrackingPackat::new, this::handle);
    }

    public void handle(TrackingPackat packet, Supplier<NetworkEvent.Context> ctx) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) return;
        ctx.get().enqueueWork(() -> {
            Entity entity = null;
            if (Minecraft.getInstance().level != null) {
                entity = Minecraft.getInstance().level.getEntity(packet.id);
            }
            if (entity instanceof LivingEntity) {
                TypeBinding binding = Register.TYPE.getOrDefault(packet.shieldType, null);
                float maxShield = binding != null ? binding.getMaxValue() : packet.shield;
                float shield = Math.min(packet.shield, maxShield);
                ((ILivingEntityA) entity).battery_shield$setShield(shield);
                ((ILivingEntityA) entity).battery_shield$setShieldType(packet.shieldType.getSerializedName());
//                ((ILivingEntityA) entity).battery_shield$setMaxShield(packet.maxShield);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void sendTrackingPacket(LivingEntity entity) {
        if (entity == null) return;
        if (EntityUtil.entityLevelIsClient(entity)) return;

        ILivingEntityA a = (ILivingEntityA) entity;
        if (entity.getType() == EntityType.PLAYER) {
            INSTANCE.send(PacketDistributor.ALL.noArg(),new TrackingPackat(entity.getId(), a.battery_shield$getShield(), a.battery_shield$getShieldType()));
        } else {
            INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new TrackingPackat(entity.getId(), a.battery_shield$getShield(), a.battery_shield$getShieldType()));
        }
    }
}
