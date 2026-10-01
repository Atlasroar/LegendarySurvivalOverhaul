package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import sfiomn.legendarysurvivaloverhaul.config.Config;

public enum MalusBodyPartEnum {
    HEAD,
    ARMS,
    BOTH_ARMS,
    CHEST,
    LEGS,
    BOTH_LEGS,
    FEET,
    BOTH_FEET;

    public java.util.List<? extends String> effects() {
        return switch (this) {
            case HEAD -> Config.Baked.headPartEffects;
            case ARMS -> Config.Baked.armsPartEffects;
            case BOTH_ARMS -> Config.Baked.bothArmsPartEffects;
            case CHEST -> Config.Baked.chestPartEffects;
            case LEGS -> Config.Baked.legsPartEffects;
            case BOTH_LEGS -> Config.Baked.bothLegsPartEffects;
            case FEET -> Config.Baked.feetPartEffects;
            case BOTH_FEET -> Config.Baked.bothFeetPartEffects;
        };
    }

    public java.util.List<? extends Integer> amplifiers() {
        return switch (this) {
            case HEAD -> Config.Baked.headPartEffectAmplifiers;
            case ARMS -> Config.Baked.armsPartEffectAmplifiers;
            case BOTH_ARMS -> Config.Baked.bothArmsPartEffectAmplifiers;
            case CHEST -> Config.Baked.chestPartEffectAmplifiers;
            case LEGS -> Config.Baked.legsPartEffectAmplifiers;
            case BOTH_LEGS -> Config.Baked.bothLegsPartEffectAmplifiers;
            case FEET -> Config.Baked.feetPartEffectAmplifiers;
            case BOTH_FEET -> Config.Baked.bothFeetPartEffectAmplifiers;
        };
    }

    public java.util.List<? extends Double> thresholds() {
        return switch (this) {
            case HEAD -> Config.Baked.headPartEffectThresholds;
            case ARMS -> Config.Baked.armsPartEffectThresholds;
            case BOTH_ARMS -> Config.Baked.bothArmsPartEffectThresholds;
            case CHEST -> Config.Baked.chestPartEffectThresholds;
            case LEGS -> Config.Baked.legsPartEffectThresholds;
            case BOTH_LEGS -> Config.Baked.bothLegsPartEffectThresholds;
            case FEET -> Config.Baked.feetPartEffectThresholds;
            case BOTH_FEET -> Config.Baked.bothFeetPartEffectThresholds;
        };
    }
}
