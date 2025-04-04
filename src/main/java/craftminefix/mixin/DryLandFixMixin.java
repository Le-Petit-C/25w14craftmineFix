package craftminefix.mixin;

import net.minecraft.class_11057;
import net.minecraft.class_11109;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(class_11109.class_11110.class)
public abstract class DryLandFixMixin {
	@Shadow @Final @Mutable private String field_59139;
	@Shadow @Final private List<class_11057> field_59143;
	@Shadow protected abstract class_11109 method_69964();
	@Shadow public abstract class_11109.class_11110 method_69943(class_11057... args);
	@Inject(method = "method_69963", at = @At("HEAD"), cancellable = true)
    private void method_69963_injection(CallbackInfoReturnable<class_11109> cir){
		if(field_59139.equals("dry_land")){
			field_59143.clear();
			method_69943(
					class_11057.method_69662(
							(serverWorld, serverPlayerEntity, itemStack) -> {
								RegistryEntry<Biome> biome = serverWorld.getBiome(serverPlayerEntity.getBlockPos());
								return
								biome.getIdAsString().contains("badlands")
								&& itemStack.isOf(Items.LAVA_BUCKET);
							}
					)
			);
		}
		cir.setReturnValue(Registry.register(Registries.field_59575, Identifier.ofVanilla(this.field_59139), this.method_69964()));
		cir.cancel();
	}
}