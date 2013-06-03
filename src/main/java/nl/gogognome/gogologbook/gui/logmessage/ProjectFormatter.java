package nl.gogognome.gogologbook.gui.logmessage;

import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.gui.beans.ObjectFormatter;
import nl.gogognome.lib.util.StringUtil;

class ProjectFormatter implements ObjectFormatter<ProjectFindResult> {
	@Override
	public String format(ProjectFindResult project) {
		return project != null ? StringUtil.nullToEmptyString(project.projectNr) + " " + StringUtil.nullToEmptyString(project.customer) + " "
				+ StringUtil.nullToEmptyString(project.town) + " " + StringUtil.nullToEmptyString(project.street) : "";
	}
}