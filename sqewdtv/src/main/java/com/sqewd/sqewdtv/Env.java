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

import java.io.File;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.sqewdtv.utils.FileUtils;
import com.sqewd.sqewdtv.utils.LogUtils;

/**
 * @author subhagho
 * 
 */
public class Env {
	private static class Constants {
		public static final String _CONFIG_DIR_WORK_ = "sqewdtv.env.directories[@work]";
		public static final String _CONFIG_DIR_TEMP_ = "sqewdtv.env.directories[@temp]";
		public static final String _CONFIG_CLEAN_TEMP_ = "sqewdtv.env.directories[@cleanOnExit]";
	}

	private static final Logger log = LoggerFactory.getLogger(Env.class);

	private ObjectState state = ObjectState.Unknown;

	private XMLConfiguration config = null;

	private String workdir = null;

	private String tempdir = null;

	private boolean cleanTempOnDispose = true;

	private void init(final String filename) throws Exception {
		try {
			log.info("Creating Global Environment from configuration. [file="
					+ filename + "]");
			config = new XMLConfiguration(filename);

			workdir = config.getString(Constants._CONFIG_DIR_WORK_);
			if (workdir == null || workdir.isEmpty())
				throw new ConfigurationException("Missing required parameter ["
						+ Constants._CONFIG_DIR_WORK_ + "]");
			File wdi = new File(workdir);
			if (!wdi.exists()) {
				log.info("Creating work drectory, [directory="
						+ wdi.getCanonicalPath() + "]");
				wdi.mkdirs();
			}
			tempdir = config.getString(Constants._CONFIG_DIR_TEMP_,
					System.getProperty("java.io.tmpdir"));
			File tdi = new File(tempdir);
			if (!tdi.exists()) {
				log.info("Creating temp drectory, [directory="
						+ tdi.getCanonicalPath() + "]");
				tdi.mkdirs();
			}
			cleanTempOnDispose = config.getBoolean(
					Constants._CONFIG_CLEAN_TEMP_, true);
			state = ObjectState.Available;

			log.info("Global Environment successfully created...");
		} catch (Exception ex) {
			LogUtils.stacktrace(log, ex);
			state = ObjectState.Exception;
			throw ex;
		}
	}

	private void dispose() {
		try {
			config = null;
			if (cleanTempOnDispose) {
				FileUtils.cleandir(new File(tempdir));
			}
		} catch (Exception ex) {
			LogUtils.stacktrace(log, ex);
		} finally {
			state = ObjectState.Disposed;
		}
	}

	/**
	 * Get the Global Configuration.
	 * 
	 * @return
	 */
	public XMLConfiguration config() {
		return this.config;
	}

	/**
	 * Get a directory name as an absolute path from the configured Work
	 * directory.
	 * 
	 * @param path
	 *            - Sub-Path.
	 * @param clean
	 *            - If directory exists, clean contents?
	 * @return
	 * @throws Exception
	 */
	public String getworkdir(final String path, final boolean clean)
			throws Exception {
		String dir = workdir + "/" + path;
		File di = new File(dir);
		if (!di.exists()) {
			di.mkdirs();
		} else if (clean) {
			FileUtils.cleandir(di);
		}

		return di.getCanonicalPath();
	}

	/**
	 * Get a file name as an absolute path from the configured Work directory.
	 * 
	 * @param filename
	 *            - File name.
	 * @param remove
	 *            - Remove file if already exists?
	 * @return
	 * @throws Exception
	 */
	public String getworkfile(final String filename, final boolean remove)
			throws Exception {
		String path = workdir + "/" + filename;
		File fi = new File(path);
		if (fi.exists() && remove) {
			fi.delete();
		}
		return fi.getCanonicalPath();
	}

	/**
	 * Get a directory name as an absolute path from the configured Temp
	 * directory.
	 * 
	 * @param path
	 *            - Sub-Path.
	 * @param clean
	 *            - If directory exists, clean contents?
	 * @return
	 * @throws Exception
	 */
	public String gettempdir(final String path, final boolean clean)
			throws Exception {
		String dir = tempdir + "/" + path;
		File di = new File(dir);
		if (!di.exists()) {
			di.mkdirs();
		} else if (clean) {
			FileUtils.cleandir(di);
		}

		return di.getCanonicalPath();
	}

	/**
	 * Get a file name as an absolute path from the configured Temp directory.
	 * 
	 * @param filename
	 *            - File name.
	 * @param remove
	 *            - Remove file if already exists?
	 * @return
	 * @throws Exception
	 */
	public String gettempfile(final String filename, final boolean remove)
			throws Exception {
		String path = tempdir + "/" + filename;
		File fi = new File(path);
		if (fi.exists() && remove) {
			fi.delete();
		}
		return fi.getCanonicalPath();
	}

	private static final Env _env_ = new Env();

	/**
	 * Initialize the Global Environment from the specified configuration file.
	 * This should be called only once during system startup.
	 * 
	 * @param configfile
	 *            - Environment configuration file.
	 * 
	 * @throws Exception
	 */
	public static final void create(final String configfile) throws Exception {
		_env_.init(configfile);
	}

	/**
	 * Get a handle to the system environment.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final Env get() throws Exception {
		if (_env_.state != ObjectState.Available)
			throw new Exception("Environment Handle is not available. [state="
					+ _env_.state.name() + "]");
		return _env_;
	}

	/**
	 * Dispose the Global Environment.
	 */
	public static final void destroy() {
		_env_.dispose();
	}
}
