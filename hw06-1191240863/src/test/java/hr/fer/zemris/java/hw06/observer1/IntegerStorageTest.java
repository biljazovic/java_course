package hr.fer.zemris.java.hw06.observer1;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegerStorageTest {

	private static class SimpleObserver implements IntegerStorageObserver {
		int count;
		
		@Override
		public void valueChanged(IntegerStorage istorage) {
			++count;
		}
		
		public int getCount() {
			return count;
		}
	}
	
	@Test
	public void testObserver() {
		SimpleObserver observer = new SimpleObserver();
		
		IntegerStorage istorage = new IntegerStorage(5);

		istorage.addObserver(observer);
		
		istorage.setValue(10); //count = 1
		istorage.setValue(10); //count still 1
		istorage.setValue(5); //count = 2
		
		istorage.removeObserver(observer);
		
		istorage.setValue(15); //count still 2
		istorage.setValue(20); //count still 2
		
		assertEquals(observer.getCount(), 2);
	}

}
