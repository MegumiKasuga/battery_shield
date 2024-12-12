package cc.xypp.battery_shield.packet;

import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.network.FriendlyByteBuf;

public class TrackingPackat {
    public int id;
    public float shield;
    // public float maxShield;
    public ShieldType shieldType;

    public TrackingPackat(int id, float shield, ShieldType shieldType) {
        this.id = id;
        this.shield = shield;
        this.shieldType = shieldType;
    }

    public TrackingPackat(FriendlyByteBuf packetBuffer) {
        this.id = packetBuffer.readInt();
        this.shield = packetBuffer.readFloat();
        this.shieldType = ShieldType.fromString(packetBuffer.toString());
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(id);
        packetBuffer.writeFloat(shield);
        packetBuffer.writeUtf(shieldType.getSerializedName());
    }
}
