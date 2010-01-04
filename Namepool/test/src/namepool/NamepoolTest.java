package namepool;

import org.junit.Assert;
import org.junit.Test;

public class NamepoolTest {

    @Test
    public void main() {
	Namepool namepool = new Namepool();

	long fooId = namepool.store("Foo"); // count == 1 (new)
	String fooValue = namepool.retrieve(fooId);
	Assert.assertEquals("Foo", fooValue);

	fooValue = namepool.retrieve(-1);
	Assert.assertEquals(null, fooValue);

	long fooId2 = namepool.store("Foo"); // count == 2 (incremented)
	Assert.assertEquals(fooId, fooId2);

	namepool.unuse(fooId); // count == 1 (decremented);
	fooValue = namepool.retrieve(fooId);
	Assert.assertEquals("Foo", fooValue);

	namepool.unuse("Foo"); // count == 0 (deleted);
	fooValue = namepool.retrieve(fooId);
	Assert.assertEquals(null, fooValue);

	namepool.unuse("NonExisting");

	long barId = namepool.store("Bar");
	String barValue = namepool.retrieve(barId);
	Assert.assertEquals("Bar", barValue);

	long cafeId = namepool.store("Cafe");
	String cafeValue = namepool.retrieve(cafeId);
	Assert.assertEquals("Cafe", cafeValue);
    }

}
