package net.deadlydiamond98.archipelago.mixin.compat.explorerscompass;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.chaosthedude.explorerscompass.util.StructureUtils;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

@Mixin(StructureUtils.class)
public class StructureUtilsGroupSearchMixin {
    @Inject(method = "getStructuresForGroup", at = @At("RETURN"), cancellable = true)
    private static void archipelago$filterBlacklistedFromGroup(ServerWorld world, Identifier groupId, CallbackInfoReturnable<List<Identifier>> cir) {
        List<Identifier> filtered = new ArrayList<>(cir.getReturnValue());
        filtered.removeIf(id -> {
            Structure structure = StructureUtils.getStructureForID(world, id);
            return structure == null || StructureUtils.structureIsBlacklisted(world, structure);
        });
        cir.setReturnValue(filtered);
    }
}
