package com.mikealbert.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ObjectUtilsTest {

	@Test
	public void propertyValuesMatchTest(){
		Employee src = new Employee();
		src.setFirstName("B_o/b");
		src.setAge(43);
		Person target = new Person();
		target.setFirstName("B-OB");
		target.setAge(43);
		boolean result = ObjectUtils.propertyValuesMatch(src, "firstName", target, "firstName",true,"-_/");
		
		Assert.assertTrue(result);
		
		result = ObjectUtils.propertyValuesMatch(src, "age", target, "age",true);
		
		Assert.assertTrue(result);
		
	}
	
	@Test
	public void getAllPropertiesTest(){
		Employee src = new Employee();
		src.setFirstName("Bob");
		src.setAge(43);
		
		List<EmployeeLocation> locs = new ArrayList<EmployeeLocation>();
		EmployeeLocation loc = new EmployeeLocation();
		loc.setLocation("North");
		locs.add(loc);
		loc = new EmployeeLocation();
		loc.setLocation("South");
		locs.add(loc);
		src.setLocs(locs);
		
		List<String> props = ObjectUtils.getAllSimplePropertyNames(src);
		
		Assert.assertTrue(props.size() == 4);
		
		props = ObjectUtils.getAllListPropertyNames(src);
		
		Assert.assertTrue(props.size() == 1);
		
		List newSource = (List) ObjectUtils.getProperty(src, props.get(0));
	
		Assert.assertTrue(newSource.size() == 2);
	}
	

	
}

