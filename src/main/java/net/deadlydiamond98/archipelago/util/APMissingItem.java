package net.deadlydiamond98.archipelago.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

/// An item that is required and (potentially) missing for a specific crafting output.
/// Used to display missing item requirements.
public record APMissingItem(
    /// The name of the AP item required for this crafting output.
    String name,
    /// The amount of this AP item that is required for this crafting output.
    /// `0` is a sentinel value indicating the item is not progressive.
    int amount,
    /// The name of the attempted crafting output
    Text attemptedCraft
) {
    public void serialize(PacketByteBuf buf) {
        buf.writeString(name);
        buf.writeInt(amount);
        buf.writeText(attemptedCraft);
    }

    public static APMissingItem deserialize(PacketByteBuf buf) {
        String name = buf.readString();
        int amount = buf.readInt();
        Text attemptedCraft = buf.readText();
        return new APMissingItem(name, amount, attemptedCraft);
    }

    public boolean isStillMissing() {
        return !(amount == 0
                ? APItemAccessUtil.hasCheck(null, name)
                : APItemAccessUtil.hasProgressiveCheck(null, name, amount));
    }
}
