package com.coderscampus.assignment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncronizedFetching {

	public static void main(String[] args) {

		Assignment8 assignment = new Assignment8();
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<CompletableFuture<List<Integer>>> allFetchedNumbers = new ArrayList<>();

		// Call 'getNumber' Method Asynchronously by 1000 irritation to fetch 1 Million
		// integer from file and put to ArrayList
		for (int i = 0; i < 1000; i++) {
			CompletableFuture<List<Integer>> fetchedNumbers = CompletableFuture
					.supplyAsync(() -> assignment.getNumbers(),executorService);
			allFetchedNumbers.add(fetchedNumbers);
		}

		// to wait for all the futures to complete and retrieve the result
		CompletableFuture<Void> allListofnumbers = CompletableFuture
				.allOf(allFetchedNumbers.toArray(new CompletableFuture[0]));
		allListofnumbers.join();

		//count numbers and find how many times are repeated in list
		Map<Integer, Integer> numberCounts = new ConcurrentHashMap<>();
		for (CompletableFuture<List<Integer>> numbers : allFetchedNumbers) {
		    List<Integer> numbersList = numbers.join();
		    for (Integer number : numbersList) {
		        numberCounts.compute(number, (key, value) -> (value == null) ? 1 : value + 1);
		    }
		}
		
		// print counted numbers to console
		int maxNumber = Collections.max(numberCounts.keySet());
		for (int i = 0; i <= maxNumber; i++) {
		    int count = numberCounts.getOrDefault(i, 0);
		    System.out.println(i + "=" + count);
		}
	}

}
