package capstone;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sorted array implementation of DataStructure
 * 
 * @author Nathan Floor
 * @author Ryan Saunders
 */
public class SortedArray extends DataStructure {
	//instance variables
	//table variables
	private Record[] sortedRecords;

	/**
	 * Creates a new sorted array.
	 * 
	 * @param size - the starting size of the array
	 */	
	public SortedArray(int size) {
		sortedRecords = new Record[size];
		totalNumberOfRecs = size;
	}
	@Override
	public void loadData(String filename) throws IncorrectNumberOfFieldsException, IOException {
		isLoading = true;
		isWalking = false;
		isRandomAccess = false;
		BufferedReader b = new BufferedReader(new FileReader(filename));
		//b.readLine();
	
		numberOfRecs = 0;
		String line;
		String[] split = null;
	
		while ((line = b.readLine()) != null) {
			split = line.split(";");
	
			try {
				sortedRecords[numberOfRecs] = new Record(split);
			} catch (ArrayIndexOutOfBoundsException e) {  // when sortedRecords too small to hold another record
				sortedRecords = extendArray(sortedRecords);
				sortedRecords[numberOfRecs] = new Record (split);;
			}
	
			// weird way of commenting out code
			/*if (false) {
				if (numberOfRecs % 100000 == 0) {
					for (int i = 0; i < split.length; i++) {
						System.out.print(split[i] + ";");
					}
					System.out.println();
				}
			}*/
			
			synchronized(this){
				numberOfRecs++;
			}
		}
		
		b.close();
			
	}
	
	@Override
	public void walkThrough() {
		isLoading = false;
		isWalking = true;
		isRandomAccess = false;
		walkCounter = 0;
		for (int i = 0; i < numberOfRecs; i++) {
			synchronized(this){
				walkCounter++;
			}
			sortedRecords[i].toString();
		}
	}
	
	@Override
	public Record getRecord(String phone) throws RecordNotFoundException {
		isLoading = false;
		isWalking = false;
		isRandomAccess = true;
		int lower = 0;
		int higher = numberOfRecs - 1;
		int mid;
		
		while (lower != higher + 1) { // i.e. while lower <= higher
			synchronized(this){
				searchCounter++;
			}

			mid = (higher + lower) / 2;
			
			if (phone.equals(sortedRecords[mid].getKey())) { // record found				
				return sortedRecords[mid];
			} else if (phone.compareTo(sortedRecords[mid].getKey()) < 0) {
				higher = mid - 1;
			} else {
				lower = mid + 1;
			}
		}
		
		// Out of while loop: implies record does not exist
		throw new RecordNotFoundException();
	}
	
	/**
	 * Gets the record string at the specified index
	 * 
	 * @param index
	 * @return
	 */
	public Record getRecord (int index) {
		return sortedRecords[index];
	}

	/**
	 * Returns a copy of the array, but with an increased size.
	 * 
	 * @param array - the array to be extended
	 * @return the extended array
	 */
	private Record[] extendArray(Record[] array) {
		Record[] extendedArray = new Record[array.length * 10];
		
		for (int i = 0; i < array.length; i++) {
			extendedArray[i] = new Record (array[i]);
		}
		
		return extendedArray;
	}
}
