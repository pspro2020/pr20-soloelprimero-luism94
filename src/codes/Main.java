package codes;

import static codes.Constants.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {

	public static void main(String[] args) {
		SearchServer server = new SearchServer();
		List<RowSearcher> matrixRows = new ArrayList<RowSearcher>();
		int row;
		int numberToSearch = new Random().nextInt(10) + 1;
		
		for (int i = 0; i < MAX_SIZE_MATRIX_ROW_COLUMN; i++) {
			matrixRows.add(new RowSearcher(i, numberToSearch));
		}
        
        try {
        	System.out.println(String.format(NUMBER_TO_SEARCH, LocalDateTime.now().format(TIME_FORMATTER), numberToSearch));
        	row = server.getThreadAdminPool().invokeAny(matrixRows);
        } catch (ExecutionException e) {
        	System.out.println(String.format(SEARCH_FAIL, LocalDateTime.now().format(TIME_FORMATTER), numberToSearch));
        } catch (InterruptedException e) {
		}
        
        try {
            server.shutdown();
        } catch (InterruptedException e) {
        	System.out.println(String.format(ADMIN_SHUT_DOWN_INTERRUPTED, LocalDateTime.now().format(TIME_FORMATTER)));
            return;
        }
	}

}
