package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class FabricDeferredRegister<T>
{
	private final Registry<T> registry;
	private final Map<String, PendingRegistration<T>> pending = new LinkedHashMap<>();
	private boolean registered;

	private FabricDeferredRegister(Registry<T> registry)
	{
		this.registry = registry;
	}

	public static <T> FabricDeferredRegister<T> create(Registry<T> registry)
	{
		return new FabricDeferredRegister<>(registry);
	}

	public Registry<T> getRegistry()
	{
		return registry;
	}

	public RegistryObject<T> register(String path, Supplier<? extends T> supplier)
	{
		if (registered)
			throw new IllegalStateException("Cannot add registrations after registry bootstrap");

		RegistryObject<T> object = new RegistryObject<>();
		if (pending.putIfAbsent(path, new PendingRegistration<>(object, supplier)) != null)
			throw new IllegalStateException("Duplicate registration: " + path);
		return object;
	}

	public void registerAll()
	{
		if (registered)
			return;

		registered = true;
		pending.forEach((path, registration) -> {
			T value = Objects.requireNonNull(registration.supplier().get(), "Registry supplier returned null: " + path);
			Registry.register(registry, new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, path), value);
			registration.object().bind(value);
		});
	}

	private record PendingRegistration<T>(RegistryObject<T> object, Supplier<? extends T> supplier)
	{
	}
}
