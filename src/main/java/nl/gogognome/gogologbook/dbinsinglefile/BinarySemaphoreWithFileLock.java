package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.Semaphore;

public class BinarySemaphoreWithFileLock {

	// Invariants maintained by acquire() and release(). (Only if release() is called exactly once after each call to acquire()).
	// semaphore.availablePermits() == 0 || semaphore.availablePermits() == 1
	// channel == null if and only if semaphore.availablePermits() == 1
	// lock == null if and only if semaphore.availablePermits() == 1

	private final Semaphore semaphore;
	private final File lockFile;
	private FileChannel channel;
	private FileLock lock;

	public BinarySemaphoreWithFileLock(File lockFile) {
		semaphore = new Semaphore(1);
		this.lockFile = lockFile;
	}

	@SuppressWarnings("resource")
	public void acquire() throws InterruptedException, IOException {
		semaphore.acquire();

		channel = new RandomAccessFile(lockFile, "rw").getChannel();
		lock = channel.lock();
	}

	public void release() throws IOException {
		lock.release();
		channel.close();

		lock = null;
		channel = null;

		semaphore.release();
	}
}
