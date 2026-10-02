package sfiomn.legendarysurvivaloverhaul.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * One-time migration of the config files written by Forge Config API Port (mod versions before 2.0.0) to Fzzy Config.
 * <p>
 * The old files used the same paths ({@code config/legendarysurvivaloverhaul/<name>.toml}) but nested tables and
 * human-readable keys. The old file is renamed to {@code <name>.toml.forge-backup} and every recognised value is
 * copied into the new config through the field validators, so out-of-range values are corrected rather than kept.
 */
final class ForgeConfigMigration
{
	private static final String MAPPING_RESOURCE = "/assets/" + LegendarySurvivalOverhaul.MOD_ID + "/forge_config_migration.json";
	private static final Pattern TABLE = Pattern.compile("^\\s*\\[([^\\[\\]]+)]\\s*$");
	private static final Pattern ENTRY = Pattern.compile("^\\s*(\"(?:[^\"\\\\]|\\\\.)*\"|[A-Za-z0-9_\\-]+)\\s*=\\s*(.+?)\\s*$");
	// Forge keys contain spaces, so they are always quoted. Fzzy Config keys are Java field names and never are.
	private static final Pattern FORGE_KEY = Pattern.compile("^\\s*\"[^\"]* [^\"]*\"\\s*=", Pattern.MULTILINE);

	private ForgeConfigMigration() {}

	/**
	 * Reads a legacy Forge config file, if present, and moves it out of the way.
	 * @return the raw values keyed by {@code table.path|Key}, or {@code null} if there is nothing to migrate
	 */
	static Map<String, String> readLegacyFile(Path configDir, String name)
	{
		Path file = configDir.resolve(name + ".toml");
		if (!Files.isRegularFile(file))
			return null;
		try
		{
			String content = Files.readString(file, StandardCharsets.UTF_8);
			if (!FORGE_KEY.matcher(content).find())
				return null;

			Map<String, String> values = new HashMap<>();
			String table = "";
			for (String line : content.split("\\R"))
			{
				String trimmed = line.trim();
				if (trimmed.isEmpty() || trimmed.startsWith("#"))
					continue;
				Matcher tableMatcher = TABLE.matcher(line);
				if (tableMatcher.matches())
				{
					table = unquotePath(tableMatcher.group(1).trim());
					continue;
				}
				Matcher entryMatcher = ENTRY.matcher(line);
				if (entryMatcher.matches())
					values.put(table + "|" + unquote(entryMatcher.group(1)), entryMatcher.group(2));
			}

			Path backup = configDir.resolve(name + ".toml.forge-backup");
			Files.move(file, backup, StandardCopyOption.REPLACE_EXISTING);
			LegendarySurvivalOverhaul.LOGGER.info("Migrating legacy Forge config {} to Fzzy Config (backup kept at {})", file.getFileName(), backup.getFileName());
			return values;
		}
		catch (IOException e)
		{
			LegendarySurvivalOverhaul.LOGGER.error("Failed to read legacy config " + file + ", defaults will be used", e);
			return null;
		}
	}

	static void apply(String name, me.fzzyhmstrs.fzzy_config.config.Config config, Map<String, String> legacyValues)
	{
		JsonArray mappings = loadMappings(name);
		if (mappings == null)
			return;

		int migrated = 0;
		for (JsonElement element : mappings)
		{
			JsonArray mapping = element.getAsJsonArray();
			String raw = legacyValues.get(mapping.get(0).getAsString() + "|" + mapping.get(1).getAsString());
			if (raw == null)
				continue;
			String fieldName = mapping.get(2).getAsString();
			try
			{
				Field field = config.getClass().getField(fieldName);
				ValidatedField<Object> validated = castField(field.get(config));
				Object value = parse(raw, validated.get());
				if (value != null)
				{
					validated.validateAndSet(value);
					migrated++;
				}
			}
			catch (ReflectiveOperationException | RuntimeException e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("Could not migrate legacy config value {}.{} = {}", name, fieldName, raw);
			}
		}
		config.save();
		LegendarySurvivalOverhaul.LOGGER.info("Migrated {} values into {}.toml", migrated, name);
	}

	@SuppressWarnings("unchecked")
	private static ValidatedField<Object> castField(Object field)
	{
		return (ValidatedField<Object>) field;
	}

	private static JsonArray loadMappings(String name)
	{
		try (InputStream stream = ForgeConfigMigration.class.getResourceAsStream(MAPPING_RESOURCE))
		{
			if (stream == null)
				return null;
			JsonObject root = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
			return root.has(name) ? root.getAsJsonArray(name) : null;
		}
		catch (IOException | RuntimeException e)
		{
			LegendarySurvivalOverhaul.LOGGER.error("Failed to read the legacy config mapping", e);
			return null;
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Object parse(String raw, Object current)
	{
		if (current instanceof Boolean)
			return Boolean.parseBoolean(raw);
		if (current instanceof Integer)
			return (int) Double.parseDouble(raw);
		if (current instanceof Double)
			return Double.parseDouble(raw);
		if (current instanceof Enum<?> enumValue)
			return Enum.valueOf((Class) enumValue.getDeclaringClass(), unquote(raw));
		if (current instanceof List<?> list)
		{
			if (!raw.startsWith("[") || !raw.endsWith("]"))
				return null;
			Object sample = list.isEmpty() ? null : list.get(0);
			List<Object> result = new ArrayList<>();
			for (String part : splitArray(raw.substring(1, raw.length() - 1)))
			{
				if (sample instanceof Integer)
					result.add((int) Double.parseDouble(part));
				else if (sample instanceof Double)
					result.add(Double.parseDouble(part));
				else
					result.add(unquote(part));
			}
			return result;
		}
		return null;
	}

	private static List<String> splitArray(String inner)
	{
		List<String> parts = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		boolean inString = false;
		for (int i = 0; i < inner.length(); i++)
		{
			char c = inner.charAt(i);
			if (c == '"' && (i == 0 || inner.charAt(i - 1) != '\\'))
				inString = !inString;
			if (c == ',' && !inString)
			{
				addPart(parts, current);
				current.setLength(0);
			}
			else
				current.append(c);
		}
		addPart(parts, current);
		return parts;
	}

	private static void addPart(List<String> parts, StringBuilder part)
	{
		String trimmed = part.toString().trim();
		if (!trimmed.isEmpty())
			parts.add(trimmed);
	}

	private static String unquotePath(String path)
	{
		StringBuilder result = new StringBuilder();
		for (String segment : path.split("\\.(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
		{
			if (result.length() > 0)
				result.append('.');
			result.append(unquote(segment.trim()));
		}
		return result.toString();
	}

	private static String unquote(String value)
	{
		String trimmed = value.trim();
		if (trimmed.length() >= 2 && (trimmed.startsWith("\"") && trimmed.endsWith("\"") || trimmed.startsWith("'") && trimmed.endsWith("'")))
			return trimmed.substring(1, trimmed.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
		return trimmed;
	}
}
