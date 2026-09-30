package sfiomn.legendarysurvivaloverhaul.registry;

public final class RegistryObject<T>
{
	private T value;

	void bind(T value)
	{
		if (this.value != null)
			throw new IllegalStateException("Registry object was bound more than once");
		this.value = value;
	}

	public T get()
	{
		if (value == null)
			throw new IllegalStateException("Registry object has not been registered");
		return value;
	}
}
