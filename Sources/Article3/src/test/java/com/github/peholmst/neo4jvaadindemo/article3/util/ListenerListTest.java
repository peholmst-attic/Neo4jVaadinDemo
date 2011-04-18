package com.github.peholmst.neo4jvaadindemo.article3.util;

import org.junit.After;
import org.jmock.Expectations;
import com.github.peholmst.neo4jvaadindemo.article3.util.ListenerList.ListenerNotifier;
import org.jmock.Mockery;
import org.junit.Test;
import org.junit.Before;

/**
 * Test-case for {@link ListenerList}.
 * 
 * @author Petter Holmström
 */
public class ListenerListTest {

    Mockery context = new Mockery();
    ListenerNotifier<String> notifier;
    /*
     * We can use Strings as listeners in order to test
     * the functionality of the list.
     */
    private ListenerList<String> listUnderTest;

    @Before
    public void setUp() {
        listUnderTest = new ListenerList<String>();
        notifier = context.mock(ListenerNotifier.class);
    }

    @After
    public void tearDown() {
        context.assertIsSatisfied();
    }

    @Test
    public void addNullListener() {
        listUnderTest.add(null);

        context.checking(new Expectations() {

            {
                never(notifier).notifyListener(with(aNull(String.class)));
            }
        });
        listUnderTest.notifyListeners(notifier);
    }

    @Test
    public void removeNullListener() {
        listUnderTest.remove(null);
        // No exception thrown
    }

    @Test
    public void addSingleListener() {
        listUnderTest.add("Hello");

        context.checking(new Expectations() {

            {
                oneOf(notifier).notifyListener("Hello");
            }
        });
        listUnderTest.notifyListeners(notifier);
    }

    @Test
    public void addSameListenerTwice() {
        listUnderTest.add("Hello");
        listUnderTest.add("Hello");

        context.checking(new Expectations() {

            {
                exactly(2).of(notifier).notifyListener("Hello");                
            }
        });
        listUnderTest.notifyListeners(notifier);
    }
    
    @Test
    public void addDifferentListeners() {
        listUnderTest.add("Hello");
        listUnderTest.add("World");

        context.checking(new Expectations() {

            {
                oneOf(notifier).notifyListener("Hello");
                oneOf(notifier).notifyListener("World");
            }
        });
        listUnderTest.notifyListeners(notifier);
    }
    
    @Test
    public void removeSingleListener() {
        listUnderTest.add("Hello");
        listUnderTest.remove("Hello");

        context.checking(new Expectations() {

            {
                never(notifier).notifyListener(with(aNull(String.class)));
            }
        });
        listUnderTest.notifyListeners(notifier);        
    }
    
    @Test
    public void testRemoveDuplicateListener() {
        listUnderTest.add("Hello");
        listUnderTest.add("Hello");
        listUnderTest.remove("Hello");
        context.checking(new Expectations() {

            {
                oneOf(notifier).notifyListener("Hello");
            }
        });
        listUnderTest.notifyListeners(notifier);
    }
}
