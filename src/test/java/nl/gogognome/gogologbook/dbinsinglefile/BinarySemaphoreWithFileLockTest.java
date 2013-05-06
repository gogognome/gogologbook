package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class BinarySemaphoreWithFileLockTest {

	private final BinarySemaphoreWithFileLock semaphore;
	private boolean lockAcquired;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			printUsage();
			return;
		}

		new BinarySemaphoreWithFileLockTest(new File(args[0])).startTerminal();
	}

	public BinarySemaphoreWithFileLockTest(File lockFile) {
		System.out.println("File to be locked: " + lockFile.getAbsolutePath());
		semaphore = new BinarySemaphoreWithFileLock(lockFile);
	}

	private void startTerminal() throws IOException, InterruptedException {
		printCommands();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (String command = reader.readLine(); command != null; command = reader.readLine()) {
				if ("acquire".equals(command)) {
					acquire();
				} else if ("release".equals(command)) {
					release();
				} else if ("stop".equals(command)) {
					break;
				} else {
					System.out.println("Unknown command: " + command);
					printCommands();
				}
			}
		} finally {
			if (lockAcquired) {
				release();
			}
			reader.close();
			System.out.println("Terminal stopped.");
		}
	}

	private void acquire() throws InterruptedException, IOException {
		if (lockAcquired) {
			System.out.println("Lock was already acquired before. Release the lock before acquiring it.");
			return;
		}

		semaphore.acquire();
		lockAcquired = true;
		System.out.println("Lock has been acquired.");
	}

	private void release() throws IOException {
		if (!lockAcquired) {
			System.out.println("Lock was not acquired before. Acquire the lock before releasing it.");
			return;
		}

		semaphore.release();
		lockAcquired = false;
		System.out.println("Lock has been released.");
	}

	private void printCommands() {
		System.out.println("Commands: acquire, release and stop");
	}

	private static void printUsage() {
		System.out.println("Application requires 1 parameter: path of the lock file.");
	}
}
