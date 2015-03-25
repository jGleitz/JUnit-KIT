package test;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * @author Joshua Gleitze
 * @version 1.0
 */
public class KITSuite extends Suite {
	private Class<?> rootClass;

	/**
	 * @param klass
	 * @param builder
	 * @throws InitializationError
	 */
	public KITSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
		this.rootClass = klass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.junit.runners.ParentRunner#run(org.junit.runner.notification.RunNotifier)
	 */
	@Override
	public void run(RunNotifier notifier) {
		OutputFileWriter.initFor(rootClass);
		notifier.addListener(OutputFileWriter.getListener());
		super.run(notifier);
	}

}
