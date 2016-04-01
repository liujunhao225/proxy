package com.proxy;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

import com.proxy.tasker.ProxyFinderTasker;

/**
 * loader all tasker and start
 * 
 * @author mayibo
 *
 */
public class ProxyMain {

	public static void main(String[] args) {

		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		Layout layout = new PatternLayout(PATTERN);

		try {
			// FileAppender fileappender = new FileAppender(layout,
			// "d://log.txt");
			ConsoleAppender console = new ConsoleAppender(); // create appender

			console.setLayout(new PatternLayout(PATTERN));
			console.setThreshold(org.apache.log4j.Level.INFO);
			console.activateOptions();
			// fileappender.setThreshold(org.apache.log4j.Level.INFO);
			// fileappender.activateOptions();
			BasicConfigurator.configure(console);

			new ProxyFinderTasker().execute();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

}
