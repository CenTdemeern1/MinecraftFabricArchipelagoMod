package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.archipelago.items.SavedArchipelagoItems;
import net.deadlydiamond98.archipelago.compat.create.IMissingAPItemAccessor;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(KineticBlockEntity.class)
public class KineticBlockEntityMixin {
    @Unique
    Optional<APItemAccessUtil.MissingItem> getMissingItem() {
        if ((Object) this instanceof IMissingAPItemAccessor accessor) {
            return accessor.archipelago$getMissingAPItem();
        }
        return Optional.empty();
    }

    @Inject(method = "addToTooltip", at = @At("RETURN"), cancellable = true, remap = false)
    void addMissingAPItemTooltip(List<Text> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        Optional<APItemAccessUtil.MissingItem> potentialMissingItem = getMissingItem();
        if (potentialMissingItem.isPresent()) {
            APItemAccessUtil.MissingItem missingItem = potentialMissingItem.get();
            String itemName = SavedArchipelagoItems.ID_TO_NAME_MAP.get(missingItem.name);
            new LangBuilder(APMod.MOD_ID)
                    .translate("check.requires_check", itemName)
                    .style(Formatting.GOLD)
                    .forGoggles(tooltip);
            String translationKey = missingItem.amount == 1
                            ? "archipelago.compat.create.crafting.requires_check"
                            : "archipelago.compat.create.crafting.requires_check.multiple";
            MutableText description = Text.translatable(translationKey, missingItem.attemptedCraft, itemName, missingItem.amount);
            List<Text> cutDescription = TooltipHelper.cutTextComponent(description, FontHelper.Palette.GRAY_AND_WHITE);
            for (Text text : cutDescription) {
                new LangBuilder(APMod.MOD_ID)
                        .add(text)
                        .forGoggles(tooltip);
            }
            cir.setReturnValue(true);
        }
    }
}
