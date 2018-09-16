package com.ots.common;
public class UserBean {

	private String id;
	private String firstName;
	private String lastName;
	private String apartmentNumber;
	private String street;
	private String city;
	private String state;
	private Integer zipcode;
	private Integer phoneNumber;
	private Integer cellPhoneNumber;
	private String emailId;
	private String password;
	private String password1;
	private String password2;
	private String companyId;
	private UserType userType;

	
	public String getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	
	public String getPassword1() {
		return password1;
	}

	
	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	
	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	
	public UserType getUserType() {
		return userType;
	}


	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipcode
	 */
	public Integer getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode
	 *            the zipcode to set
	 */
	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the phoneNumber
	 */
	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
	public Integer getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(Integer cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	
	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "UserBean [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", apartmentNumber="
				+ apartmentNumber + ", street=" + street + ", city=" + city + ", state=" + state + ", zipcode="
				+ zipcode + ", phoneNumber=" + phoneNumber + ", cellPhoneNumber=" + cellPhoneNumber + ", emailId="
				+ emailId + ", password=" + password + ", password1=" + password1 + ", password2=" + password2
				+ ", userType=" + userType + "]";
	}

}
