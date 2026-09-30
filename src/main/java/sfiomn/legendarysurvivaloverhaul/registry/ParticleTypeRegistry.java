package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ParticleTypeRegistry {
    public static final FabricDeferredRegister<ParticleType<?>> PARTICLE_TYPES = FabricDeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE);

    public static final RegistryObject<SimpleParticleType> SUN_FERN_BLOSSOM = PARTICLE_TYPES.register("sun_fern_blossom", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ICE_FERN_BLOSSOM = PARTICLE_TYPES.register("ice_fern_blossom", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> COLD_BREATH = PARTICLE_TYPES.register("cold_breath", () -> new SimpleParticleType(true));

    public static void register() {
        PARTICLE_TYPES.registerAll();
    }
}
