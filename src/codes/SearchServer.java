package codes;

import static codes.Constants.*;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchServer {
	
	private static class SearcherThreadFactory implements ThreadFactory {
		int numOfThreads = 1;
		
		@Override
		public Thread newThread(Runnable searchThread) {
			return new Thread(searchThread, String.format(THREAD_NAME, numOfThreads++));
		}
		
	}
	
	// Objeto ThreadPoolExecutor para que administre la ejecucion de los hilos
	// secundarios atendiendo al numero maximo de nucleos
	// del sistema
	private final float CORE_PROCESSOR_NUMBER = Runtime.getRuntime().availableProcessors();
	private final ThreadPoolExecutor threadAdminPool = 
			(ThreadPoolExecutor) Executors.newFixedThreadPool((int) CORE_PROCESSOR_NUMBER);

	public ThreadPoolExecutor getThreadAdminPool() {
		return threadAdminPool;
	}

	public SearchServer() {
		threadAdminPool.setThreadFactory(new SearcherThreadFactory());
		threadAdminPool.setRejectedExecutionHandler((taskThread, poolExecutor) -> 
			System.out.println(String.format(SEARCH_REJECTED, LocalDateTime.now().format(TIME_FORMATTER), ((Thread) taskThread).getName())));
	}
	
    public void shutdown() throws InterruptedException {
        threadAdminPool.shutdown();
        if (threadAdminPool.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println(String.format(ADMIN_SHUT_DOWN_COMPLETED, LocalDateTime.now().format(TIME_FORMATTER), threadAdminPool.getLargestPoolSize()));
        } else {
            System.out.println(String.format(ADMIN_SHUT_DOWN_TIMEOUT, LocalDateTime.now().format(TIME_FORMATTER), threadAdminPool.getLargestPoolSize()));
        }
    }

    public void shutdownNow() throws InterruptedException {
        threadAdminPool.shutdownNow();
        if (threadAdminPool.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println(String.format(ADMIN_SHUT_DOWN_COMPLETED, LocalDateTime.now().format(TIME_FORMATTER), threadAdminPool.getLargestPoolSize()));
        } else {
            System.out.println(String.format(ADMIN_SHUT_DOWN_TIMEOUT, LocalDateTime.now().format(TIME_FORMATTER), threadAdminPool.getLargestPoolSize()));
        }
    }
}
