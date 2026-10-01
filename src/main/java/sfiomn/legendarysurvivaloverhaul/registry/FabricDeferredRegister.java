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

	public <R extends T> RegistryObject<R> register(String path, Supplier<? extends R> supplier)
	{
		if (registered)
			throw new IllegalStateException("Cannot add registrations after registry bootstrap");

		RegistryObject<R> object = new RegistryObject<>();
		if (pending.putIfAbsent(path, new TypedPendingRegistration<>(object, supplier)) != null)
			throw new IllegalStateException("Duplicate registration: " + path);
		return object;
	}

	public void registerAll()
	{
		if (registered)
			return;

		registered = true;
		pending.forEach((path, registration) -> registration.register(registry,
				new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, path), path));
	}

	private interface PendingRegistration<T>
	{
		void register(Registry<T> registry, ResourceLocation id, String path);
	}

	private record TypedPendingRegistration<T, R extends T>(RegistryObject<R> object, Supplier<? extends R> supplier) implements PendingRegistration<T>
	{
		@Override
		public void register(Registry<T> registry, ResourceLocation id, String path)
		{
			R value = Objects.requireNonNull(supplier.get(), "Registry supplier returned null: " + path);
			Registry.register(registry, id, value);
			object.bind(value);
		}
	}
}
