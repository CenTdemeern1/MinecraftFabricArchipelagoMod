package net.deadlydiamond98.archipelago.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Optional;

// I would have used a record, but I don't want to write getters for all the methods
/// An item that is required and (potentially) missing for a specific crafting output.
/// Used to display missing item requirements.
public final class APMissingItem {
    /// The name of the AP item required for this crafting output
    public String name;
    /// The amount of this AP item that is required for this crafting output
    public int amount;
    /// The name of the attempted crafting output
    public Text attemptedCraft;
    /// Whether the required AP item is actually missing.
    /// Sometimes false if multiple requirements need to be displayed.
    public boolean isMissing;

    APMissingItem(String name, int amount, Text attemptedCraft) {
        this.name = name;
        this.amount = amount;
        this.attemptedCraft = attemptedCraft;
        this.isMissing = true;
    }

    APMissingItem(String name, int amount, Text attemptedCraft, boolean isMissing) {
        this.name = name;
        this.amount = amount;
        this.attemptedCraft = attemptedCraft;
        this.isMissing = isMissing;
    }

    public void serialize(PacketByteBuf buf) {
        buf.writeString(name);
        buf.writeInt(amount);
        buf.writeText(attemptedCraft);
        buf.writeBoolean(isMissing);
    }

    public static APMissingItem deserialize(PacketByteBuf buf) {
        String name = buf.readString();
        int amount = buf.readInt();
        Text attemptedCraft = buf.readText();
        boolean isMissing = buf.readBoolean();
        return new APMissingItem(name, amount, attemptedCraft, isMissing);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof APMissingItem otherItem
                && this.name.equals(otherItem.name)
                && this.amount == otherItem.amount
                && this.attemptedCraft.equals(otherItem.attemptedCraft)
                && this.isMissing == otherItem.isMissing;
    }
}
