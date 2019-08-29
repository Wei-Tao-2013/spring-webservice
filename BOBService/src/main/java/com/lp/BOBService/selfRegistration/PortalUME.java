package com.lp.BOBService.selfRegistration;

import com.lp.connector.exception.ConnectorException;
import com.lp.BOBService.model.RegistrationRequest;
import com.lp.BOBService.model.RegistrationResponse;
import com.sap.security.api.IUserMaint;

public abstract interface PortalUME {
	public abstract RegistrationResponse initPortalUser(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse VerifyInitPortalUser(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse getEncryptPwd(String logonId) throws ConnectorException;

	public abstract RegistrationResponse validateAccount(String logonId, String pwd) throws ConnectorException;

	public abstract RegistrationResponse assignPendingGroup(String logonId) throws ConnectorException;

	public abstract RegistrationResponse assignApprovalGroup(String logonId) throws ConnectorException;

	public abstract String checkGroup(String logonId);

	public abstract boolean checkDriverGroup(String logonId);

	public abstract void removeGroup(String logonId, String groupUniqueName);

	public abstract void updateUserDepartment(String logonId);

	public abstract IUserMaint getUserInfo(String logonId);
	
	public abstract RegistrationResponse resetPassword(RegistrationRequest paramRegistration)
			throws ConnectorException;

	

}
