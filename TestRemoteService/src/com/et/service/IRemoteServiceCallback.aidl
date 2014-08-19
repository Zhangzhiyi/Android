package com.et.service;

import com.et.service.Product;
import com.et.TestRemoteService.Book;
interface IRemoteServiceCallback{

	void valueChanged(int value);
	
	void productChanged(inout Product product);
	
	void productListChanged(inout List<Product> list);
	
	void bookChanged(inout Book book);
}