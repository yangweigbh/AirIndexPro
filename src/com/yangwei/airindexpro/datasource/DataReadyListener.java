package com.yangwei.airindexpro.datasource;

public interface DataReadyListener<T> {
	void dataReady(T data);
}
