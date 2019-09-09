package com.lp.BOBService.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Object of web service
 * 
 * @author taowe
 * @since 10-Nov-2016
 */

@Data @NoArgsConstructor
public class Address implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 7304869521285729271L;

	private String streetaddressLong;
	private String routeLong;
	private String intersectionLong;
	private String politicalLong;
	private String countryLong;
	private String administrativearealevel1Long;
	private String administrativearealevel2Long;
	private String administrativearealevel3Long;
	private String administrativearealevel4Long;
	private String administrativearealevel5Long;
	private String colloquialareaLong;
	private String localityLong;
	private String sublocalitylevel1Long;
	private String sublocalitylevel2Long;
	private String sublocalitylevel3Long;
	private String sublocalitylevel4Long;
	private String sublocalitylevel5Long;
	private String neighborhoodLong;
	private String premiseLong;
	private String subpremiseLong;
	private String postalcodeLong;
	private String naturalfeatureLong;
	private String airportLong;
	private String parkLong;
	private String postboxLong;
	private String streetnumberLong;
	private String floorLong;
	private String roomLong;

	private String streetaddressShort;
	private String routeShort;
	private String intersectionShort;
	private String politicalShort;
	private String countryShort;
	private String administrativearealevel1Short;
	private String administrativearealevel2Short;
	private String administrativearealevel3Short;
	private String administrativearealevel4Short;
	private String administrativearealevel5Short;
	private String colloquialareaShort;
	private String localityShort;
	private String sublocalitylevel1Short;
	private String sublocalitylevel2Short;
	private String sublocalitylevel3Short;
	private String sublocalitylevel4Short;
	private String sublocalitylevel5Short;
	private String neighborhoodShort;
	private String premiseShort;
	private String subpremiseShort;
	private String postalcodeShort;
	private String naturalfeatureShort;
	private String airportShort;
	private String parkShort;
	private String postboxShort;
	private String streetnumberShort;
	private String floorShort;
	private String roomShort;

	
}
