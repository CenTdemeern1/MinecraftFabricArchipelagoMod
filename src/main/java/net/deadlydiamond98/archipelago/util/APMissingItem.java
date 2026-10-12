package net.deadlydiamond98.archipelago.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Optional;

public final class APMissingItem {
    public String name;
    public int amount;
    public Text attemptedCraft;

    APMissingItem(String name, int amount, Text attemptedCraft) {
        this.name = name;
        this.amount = amount;
        this.attemptedCraft = attemptedCraft;
    }

    public void serialize(PacketByteBuf buf) {
        buf.writeInt(amount);
        buf.writeString(name);
        buf.writeText(attemptedCraft);
    }

    public static void serializeNone(PacketByteBuf buf) {
        buf.writeInt(0);
    }

    public static Optional<APMissingItem> deserialize(PacketByteBuf buf) {
        int amount = buf.readInt();
        if (amount == 0) {
            return Optional.empty();
        }
        String name = buf.readString();
        Text attemptedCraft = buf.readText();
        return Optional.of(new APMissingItem(name, amount, attemptedCraft));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof APMissingItem otherItem
                && this.name.equals(otherItem.name)
                && this.amount == otherItem.amount
                && this.attemptedCraft.equals(otherItem.attemptedCraft);
    }
}
