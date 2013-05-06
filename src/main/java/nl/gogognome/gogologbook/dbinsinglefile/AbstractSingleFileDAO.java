package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

public class AbstractSingleFileDAO {

	protected final File dbFile;

	protected AbstractSingleFileDAO(File dbFile) {
		this.dbFile = dbFile;
	}

	private static final Map<File, BinarySemaphoreWithFileLock> fileToSemaphoreMap = Maps.newHashMap();

	protected void acquireLock() {
		createSemaphoreIfNeeded();
		try {
			fileToSemaphoreMap.get(dbFile).acquire();
		} catch (Exception e) {
			throw new RuntimeException("Problem occurred while releasing lock", e);
		}
	}

	private void createSemaphoreIfNeeded() {
		if (fileToSemaphoreMap.containsKey(dbFile)) {
			return;
		}

		fileToSemaphoreMap.put(dbFile, new BinarySemaphoreWithFileLock(new File(dbFile.getAbsoluteFile() + ".lock")));
	}

	protected void releaseLock() {
		try {
			fileToSemaphoreMap.get(dbFile).release();
		} catch (Exception e) {
			throw new RuntimeException("Problem occurred while releasing lock", e);
		}
	}

}
