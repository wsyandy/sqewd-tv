/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.sqewdtv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sqewd.sqewdtv.utils.LogUtils;
import com.sqewd.sqewdtv.utils.XMLUtils;

/**
 * Factory Class for loading and managing the life-cycle of widget processes.
 * 
 * @author subhagho
 * 
 */
public class WidgetProcessManager {
	private static final class Constants {
		public static final String _CONFIG_XPATH_WIDGET_ = "/configuration/sqewdtv/widgets/widget";
		public static final String _CONFIG_WIDGET_ATTR_NAME_ = "name";
		public static final String _CONFIG_WIDGET_ATTR_CLASS_ = "class";
	}

	private static final class Runner implements Runnable {
		private static final Logger log = LoggerFactory.getLogger(Runner.class);

		private static final long _SLEEP_INTERVAL_ = 1000; // 1 sec.

		private WidgetProcessManager parent = null;

		private ObjectState state = ObjectState.Unknown;

		private Runner(final WidgetProcessManager parent) {
			this.parent = parent;
			this.state = ObjectState.Available;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			log.info("Starting Widget Refresh thread...");
			try {
				state = ObjectState.Executing;
				while (true) {
					if (state != ObjectState.Available) {
						log.info("Stopping Widget Refresh thread. [state="
								+ state.name() + "]");
						break;
					}

					List<AbstractWidgetProcess> wps = parent.widgetsToRun();
					if (wps != null && wps.size() > 0) {
						for (AbstractWidgetProcess wp : wps) {
							try {
								wp.run();
							} catch (Exception re) {
								log.warn("Widget [name="
										+ wp.getName()
										+ "] raised excpetion. Terminating widget...");
								wp.setState(ObjectState.Exception);
							}
						}
					}
					Thread.sleep(_SLEEP_INTERVAL_);
				}
			} catch (Exception e) {
				LogUtils.stacktrace(log, e);
				log.error(e.getLocalizedMessage());
				state = ObjectState.Exception;
			}
		}

	}

	private static final Logger log = LoggerFactory
			.getLogger(WidgetProcessManager.class);

	private HashMap<String, AbstractWidgetProcess> widgets = new HashMap<String, AbstractWidgetProcess>();

	private ObjectState state = ObjectState.Unknown;

	private Runner runner = new Runner(this);

	private void init() throws Exception {
		try {
			log.info("Initializing Widget Factory...");

			XMLConfiguration config = Env.get().config();

			Document doc = config.getDocument();

			NodeList wnodes = XMLUtils.search(Constants._CONFIG_XPATH_WIDGET_,
					doc.getDocumentElement());
			if (wnodes != null && wnodes.getLength() > 0) {
				for (int ii = 0; ii < wnodes.getLength(); ii++) {
					Element node = (Element) wnodes.item(ii);
					AbstractWidgetProcess wp = load(doc, node);
					widgets.put(wp.getName(), wp);
				}
			}
			log.info("Widget Processes loaded, [count=" + widgets.size() + "]");
			state = ObjectState.Available;
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			state = ObjectState.Exception;
			throw e;
		}
	}

	private AbstractWidgetProcess load(final Document doc, final Element parent)
			throws Exception {
		String wn = parent.getAttribute(Constants._CONFIG_WIDGET_ATTR_NAME_);
		String wc = parent.getAttribute(Constants._CONFIG_WIDGET_ATTR_CLASS_);

		Class<?> wcls = Class.forName(wc);
		Object wobj = wcls.newInstance();
		if (!(wobj instanceof AbstractWidgetProcess))
			throw new ConfigurationException("Class does not inherit from ["
					+ AbstractWidgetProcess.class.getCanonicalName()
					+ ". [class=" + wcls.getCanonicalName() + "]");
		AbstractWidgetProcess wp = (AbstractWidgetProcess) wobj;

		wp.setName(wn);
		wp.init(Env.get().config());

		return wp;
	}

	private void stop(final String name) {
		if (widgets.containsKey(name)) {
			AbstractWidgetProcess wp = widgets.get(name);
			wp.stop();
		}
	}

	private void stop() {
		if (widgets.size() > 0) {
			for (String key : widgets.keySet()) {
				stop(key);
			}
		}
		if (runner != null) {
			runner.state = ObjectState.Disposed;
		}
	}

	private void start() throws Exception {
		if (state != ObjectState.Available)
			throw new Exception("Widget Process Manager not available. [state="
					+ state.name() + "]");

		if (runner.state == ObjectState.Executing)
			throw new Exception("Widget Refresh thread already running...");
		Thread th = new Thread(runner);
		th.start();
		th.join();
	}

	private List<AbstractWidgetProcess> widgetsToRun() throws Exception {
		if (state != ObjectState.Available)
			throw new Exception("Object not available. [state=" + state.name()
					+ "]");
		List<AbstractWidgetProcess> wps = new ArrayList<AbstractWidgetProcess>();
		synchronized (widgets) {
			for (String key : widgets.keySet()) {
				AbstractWidgetProcess wp = widgets.get(key);
				if (wp.getState() == ObjectState.Available && wp.doRun()) {
					wps.add(wp);
				}
			}
		}
		return wps;
	}

	private static final WidgetProcessManager _factory_ = new WidgetProcessManager();

	/**
	 * Start the Widget Refresh process.
	 * 
	 * @throws Exception
	 */
	public static final void run() throws Exception {
		_factory_.start();
	}

	/**
	 * Dispose this Widget Process Manager.
	 */
	public static final void dispose() {
		_factory_.stop();
		_factory_.widgets.clear();
	}

	/**
	 * Get the Widget Process handle specified by the name.
	 * 
	 * @param name
	 *            - Widget Process name.
	 * @return
	 */
	public static final AbstractWidgetProcess getWidgetProcess(final String name) {
		if (_factory_.widgets.containsKey(name))
			return _factory_.widgets.get(name);
		return null;
	}

	/**
	 * Programmatically add a new Widget Process.
	 * 
	 * @param wp
	 * @throws Exception
	 */
	public static final void addWidgetProcess(final AbstractWidgetProcess wp)
			throws Exception {
		synchronized (_factory_.widgets) {
			if (_factory_.widgets.containsKey(wp.getName()))
				throw new Exception("Widget with name already exists. [name="
						+ wp.getName() + "]");
			_factory_.widgets.put(wp.getName(), wp);
		}
	}

	/**
	 * Programmatically remove a Widget Process.
	 * 
	 * @param name
	 *            - Name of the widget process to remove.
	 */
	public static final void removeWidgetProcess(final String name) {
		synchronized (_factory_.widgets) {
			if (_factory_.widgets.containsKey(name)) {
				_factory_.widgets.remove(name);
			}
		}
	}

	/**
	 * Load the widget factory with the configured widgets.
	 * 
	 * @throws Exception
	 */
	public static final void load() throws Exception {
		_factory_.init();
	}
}
