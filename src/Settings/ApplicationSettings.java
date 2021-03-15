package Settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.serialize.SerializationException;

import Database.Librarian;


public class ApplicationSettings {

	public static Librarian authorizedLibrarian = null;

	private static JacksonConfigurationLoader loader;
	private static BasicConfigurationNode node;
	public static MyConfiguration Configuration;
	static {
		final Path file = Paths.get("settins.json");
		loader = JacksonConfigurationLoader.builder()
				.defaultOptions(opts -> opts.shouldCopyDefaults(true)).path(file).build();

		try {
			node = loader.load();// Load from file
			Configuration = node.get(MyConfiguration.class); // Populate object
		} catch (ConfigurateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void save()
	{
		try {
			node.set(MyConfiguration.class, Configuration); // Update the backing node
			loader.save(node); // Write to the original file
		} catch (SerializationException e) {
			e.printStackTrace();
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

	@ConfigSerializable
	private static class MyConfiguration {
		@Comment("Разрешить редактирование таблицы каталога книг")
		public boolean enableEditCatalogView = false;
		@Comment("Разрешить редактирование таблицы читателей")
		public boolean enableEditReadersView = false;
		@Comment("Сохранять ширину столбцов")
		public boolean saveTablesColumns = false;
		@Comment("Показывать подписи кнопок главного меню")
		public boolean showMainMenuButtonsTitles = true;
		@Comment("Показывать подписи кнопок меню таблиц")
		public boolean showTablesMenuButtonsTitles = true;
		
		@Comment("Размеры столбцов таблицы Catalog")
		public List<Integer> catalogColumnsSizes = new ArrayList<>();
		@Comment("Размеры столбцов таблицы Formulars")
		public List<Integer> formularsColumnsSizes = new ArrayList<>();
		@Comment("Размеры столбцов таблицы читателей")
		public List<Integer> readersColumnsSizes = new ArrayList<>();
		@Comment("Размеры столбцов таблицы Учетных записей")
		public List<Integer> librariansColumnsSizes = new ArrayList<>();
	}
}
