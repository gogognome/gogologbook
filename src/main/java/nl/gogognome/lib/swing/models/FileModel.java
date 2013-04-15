/*
   Copyright 2011 Sander Kooijmans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.gogognome.lib.swing.models;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import nl.gogognome.lib.util.ComparatorUtil;

/**
 * This class implements a model to select a file.
 * A button id and file filter can be set. These are used
 * by the FileSelectionBean when the file chooser dialog is shown.
 *
 * @author Sander Kooijmans
 */
public class FileModel extends AbstractModel {

	private File file;
	private String buttonId = "gen.choose";
	private FileFilter fileFilter;

	public FileModel() {
		this(null);
	}

	public FileModel(File file) {
		super();
		this.file = file;
	}

	public File getFile() {
		return file;
	}

    /**
     * Sets the file of this model.
     * @param newFile the new value of the file
     * @param source the model change listener that sets the file. It will not
     *         get notified. It may be <code>null</code>.
     */
	public void setFile(File newFile, ModelChangeListener source) {
        File oldFile = this.file;
        if (!ComparatorUtil.equals(oldFile, newFile)) {
            this.file = newFile;
            notifyListeners(source);
        }
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public FileFilter getFileFilter() {
		return fileFilter;
	}

	public void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}
}
