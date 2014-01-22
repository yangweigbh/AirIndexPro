package com.yangwei.airindexpro.datasource;

import java.util.LinkedList;

public interface IDataSource<T> {

	void getData(DataReadyListener<LinkedList<T>> listener);
	
	void setCity(String city);
}
