package net.deadlydiamond98.archipelago.mixin.compat.emi;

import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.ItemEmiStack;
import net.deadlydiamond98.archipelago.client.ArchipelagoItemIconRenderer;
import net.deadlydiamond98.archipelago.util.tracker.ArchipelagoTrackingData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEmiStack.class)
public class ItemEmiStackMixin {
    @Inject(method = "render", at = @At("TAIL"), remap = false)
    void archipelago$renderIcon(DrawContext draw, int x, int y, float delta, int flags, CallbackInfo ci, @Local(name = "stack") ItemStack stack) {
        if (ArchipelagoTrackingData.UNCHECKED_ITEMS.contains(stack.getItem())) {
            ArchipelagoItemIconRenderer.renderIconHandledScreen(draw.getMatrices(), draw.getVertexConsumers(), x, y);
        }
    }
}
