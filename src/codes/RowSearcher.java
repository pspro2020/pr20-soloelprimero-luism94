package codes;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static codes.Constants.*;

public class RowSearcher implements Callable<Integer> {
	
	private int rowIndex;
	private int[] matrixRow = new int[MAX_SIZE_MATRIX_ROW_COLUMN];
	private Random random = new Random();
	private int numberToSearch;
	
	public RowSearcher(int rowIndex, int numberToSearch) {
		this.rowIndex = rowIndex;
		this.numberToSearch = numberToSearch;
		for (int i = 0; i < MAX_SIZE_MATRIX_ROW_COLUMN; i++) {
			matrixRow[i] = random.nextInt(10) + 1;
			System.out.print(String.format(ROW_NUMBER, matrixRow[i]));
		}
		System.out.println();
	}

	//Metodo que calcula la suma total de la fila de la matriz
	private Integer getRowPosition() throws InterruptedException {
		int rowPosition = -1;
		int numberIndex = 0;
		boolean found = false;
		
		while (!Thread.currentThread().isInterrupted() && !found && numberIndex < MAX_SIZE_MATRIX_ROW_COLUMN) {
			TimeUnit.MILLISECONDS.sleep(((long)random.nextInt(501) + 500));
			if (numberToSearch == matrixRow[numberIndex]) {
				rowPosition = numberIndex;
				found = true;
			}
			numberIndex++;
		}
		
		if (rowPosition != -1) {
			System.out.println(String.format(NUMBER_FOUND, LocalDateTime.now().format(TIME_FORMATTER), Thread.currentThread().getName(), rowIndex+1));
		}
		
		return rowPosition;
	}

	@Override
	public Integer call() throws Exception {
		int position = -1;
		try {
			position = getRowPosition();
			if (position == -1) {
				throw new RuntimeException();
			}
			
		} catch (InterruptedException e) {
			System.out.println(String.format(Constants.SEARCH_INTERRUPTED, LocalDateTime.now().format(TIME_FORMATTER), Thread.currentThread().getName(), rowIndex+1));
		} catch (RuntimeException e) {
			System.out.println(String.format(NUMBER_NOT_FOUND, LocalDateTime.now().format(TIME_FORMATTER), Thread.currentThread().getName(), rowIndex+1));
			throw new RuntimeException();
			
		}
		return position;
	}
}
