package cc.xypp.battery_shield.packet;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.utils.TypeBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateConfigPacket {

    private final int shield_pre_level, battery_value, small_battery_value, shield_cooldown;
    private final boolean zero_damage_event, calc_damage_with_event;

    public UpdateConfigPacket() {
        this.shield_pre_level = Config.shield_pre_level;
        this.battery_value = Config.battery_value;
        this.small_battery_value = Config.small_battery_value;
        this.shield_cooldown = Config.shield_cooldown;
        this.zero_damage_event = Config.zero_damage_event;
        this.calc_damage_with_event = Config.calc_damage_with_event;
    }

    public UpdateConfigPacket(FriendlyByteBuf buf) {
        shield_pre_level = buf.readInt();
        battery_value = buf.readInt();
        small_battery_value = buf.readInt();
        shield_cooldown = buf.readInt();
        zero_damage_event = buf.readBoolean();
        calc_damage_with_event = buf.readBoolean();
    }

    public static void handle(UpdateConfigPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) return;
        ctx.get().enqueueWork(() -> {
            Config.shield_pre_level = packet.shield_pre_level;
            Config.shield_cooldown = packet.shield_cooldown;
            Config.battery_value = packet.battery_value;
            Config.small_battery_value = packet.small_battery_value;
            Config.zero_damage_event = packet.zero_damage_event;
            Config.calc_damage_with_event = packet.calc_damage_with_event;
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(shield_pre_level);
        buf.writeInt(battery_value);
        buf.writeInt(small_battery_value);
        buf.writeInt(shield_cooldown);

        buf.writeBoolean(zero_damage_event);
        buf.writeBoolean(calc_damage_with_event);
    }
}
