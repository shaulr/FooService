package com.tikal.fooservice;
import com.tikal.fooservice.Bar;
import com.tikal.fooservice.IFooResponseListner;

interface IFooService {
    void save(inout Bar bar);
	Bar getById(int id);
	void delete(in Bar bar);
	List<Bar> getAll();
	oneway void asyncGetList(in IFooResponseListner listener);
}
