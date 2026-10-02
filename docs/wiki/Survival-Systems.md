# Survival Systems

LSO's survival mechanics are configurable. The following describes the Fabric release's systems, not a fixed difficulty preset.

## Temperature and wetness

Temperature responds to the environment and equipment. Insulated armor, coats, temperature consumables, and heater/chiller blocks help manage dangerous cold and heat.

The HUD includes temperature and wetness indicators. Dangerous temperatures can trigger warning overlays, sounds, and secondary effects. Heat-driven thirst and Heat Stroke are implemented, as are frostbite warnings and Cold Hunger.

**Cold Hunger is temperature-managed:** applying it with `/effect` outside dangerous cold does not keep it active for the requested duration; LSO clears it when the temperature condition ends.

Death respawn grants configured temperature immunity (90 seconds by default), including across dimension transitions.

## Hydration

Hydration and saturation are tracked alongside food. Use drinking, purification, canteens, and configured consumables. Jumping, successful block breaking, and attacking can consume hydration according to settings; Creative/Spectator are excluded from these tested exhaustion paths.

Consumable tooltips show hydration/saturation and effects. Low hydration can apply configurable vision blur. The Water Purifier blocks the LSO Thirst effect when detected in either hand or a Trinkets slot.

## Localized body damage

Damage can affect individual body parts and trigger configured secondary effects. Open the limb-health screen with **H** by default, or rebind it in Controls. Healing items can target injuries; First Aid Supplies held or equipped are recognized.

Body-part health and effects are configurable. Data-driven damage-source rules and equipment resistance complement the settings. Projectile headshots and the Vulnerability/Hard Falling adjustments were tested in-game.

## Health

The health overhaul supports additional, broken, resilient, permanent, and shield hearts. When enabled/configured, Absorption converts to shield health instead of applying vanilla Absorption. Shields absorb damage before remaining player/body-part damage.

Broken hearts occupy health containers; shield hearts use separate yellow/orange rows and reserve space above them for armor. Optional Overflowing Bars spacing was tested. Sleeping can restore configured player and body-part health.

## Equipment and survival blocks

Wearable accessories use Trinkets. Vanilla equipment and LSO accessories can receive datapack-driven temperature and body-part resistance.

The sewing table supports warm/cold string recipes and coat application to armor. Heater/chiller fuel operation and fuel persistence across save/reload were tested; heater multiblock drops were also checked.

For breathing equipment and air hazards, see [Air Quality and Breathing](Air-Quality-and-Breathing).
