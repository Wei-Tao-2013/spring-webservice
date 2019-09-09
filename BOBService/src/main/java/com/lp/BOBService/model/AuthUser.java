package com.lp.BOBService.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Object of web service
 * 
 * @author taowe
 * @since 10-Nov-2019
 */

@Data
@NoArgsConstructor
public class AuthUser implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 2106633320939583666L;

	private String userName;
	private String logonId;
	private String emailAddress;
	private String authentication; // true vs false
	private String status;

}
