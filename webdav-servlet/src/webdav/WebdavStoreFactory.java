package webdav;

public class WebdavStoreFactory {

	private Class fImplementation;

	public WebdavStoreFactory(Class class1) {
		fImplementation = class1;
	}

	public WebdavStorage getStore() throws InstantiationException, IllegalAccessException {
		return (WebdavStorage) fImplementation.newInstance();
	}

}
