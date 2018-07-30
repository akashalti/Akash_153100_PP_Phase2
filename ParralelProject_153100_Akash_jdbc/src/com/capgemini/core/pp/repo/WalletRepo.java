package com.capgemini.core.pp.repo;

import com.capgemini.core.pp.beans.Customer;
import com.capgemini.core.pp.exception.InvalidInputException;

public interface WalletRepo {

	public boolean save(Customer customer) throws InvalidInputException;
	public Customer findOne(String mobileNo) throws InvalidInputException;
	public Customer update(Customer customerTarget) throws InvalidInputException;
}
