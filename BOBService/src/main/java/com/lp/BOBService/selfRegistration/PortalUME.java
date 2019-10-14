package com.lp.BOBService.selfRegistration;

import com.lp.connector.exception.ConnectorException;
import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;
import com.sap.security.api.IUserMaint;

public abstract interface PortalUME {
	public abstract Response initPortalUser(Request paramRegistration) throws ConnectorException;

	public abstract Response createUMEIdentity(Request paramRegistration) throws ConnectorException;

	public abstract Response VerifyInitPortalUser(Request paramRegistration) throws ConnectorException;

	public abstract Response getEncryptPwd(String logonId) throws ConnectorException;

	public abstract Response validateAccount(String logonId, String pwd) throws ConnectorException;

	public abstract Response assignPendingGroup(String logonId) throws ConnectorException;

	public abstract Response assignApprovalGroup(String logonId) throws ConnectorException;

	public abstract String checkGroup(String logonId);

	public abstract boolean checkDriverGroup(String logonId);

	public abstract void removeGroup(String logonId, String groupUniqueName);

	public abstract void updateUserDepartment(String logonId);

	public abstract IUserMaint getUserInfo(String logonId);

	public abstract boolean checkUserExist(String logonId);

	public abstract Response resetPassword(Request paramRegistration) throws ConnectorException;

}
