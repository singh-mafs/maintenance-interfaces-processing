package com.mikealbert.batch.writers;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.mikealbert.data.vo.StoreLocationVO;

public class StoreLocationWriterMock implements ItemWriter<StoreLocationVO> {
	private List<StoreLocationVO> assertionList;
	
	public void write(List<? extends StoreLocationVO> items) throws Exception {
		assertionList = (List<StoreLocationVO>) items;
	}

	public List<StoreLocationVO> getAssertionList() {
		return assertionList;
	}

}