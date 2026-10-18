package net.deadlydiamond98.archipelago.util.mixinterfaces;

import net.deadlydiamond98.archipelago.compat.create.requirements.CreateDisplayableAPRequirement;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public interface IDisplayableRequirementAccessor {
    CreateDisplayableAPRequirement archipelago$getDisplayableRequirement();
    void archipelago$setDisplayableRequirement(CreateDisplayableAPRequirement requirement);
}
