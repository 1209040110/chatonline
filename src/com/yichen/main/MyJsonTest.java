package com.yichen.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MyJsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Person p1=new Person();
		p1.setAge(20);
		p1.setName("maxinyi");
		System.out.println(JSONObject.fromObject(p1).toString());
		Person p2=new Person();
		p2.setAge(50);
		p2.setName("baba");
		List<Person> personlist=new ArrayList<Person>();
		personlist.add(p1);
		personlist.add(p2);
		System.out.println(JSONArray.fromObject(personlist).toString());
		Map<String,Person> personMap=new HashMap<String, Person>();
		personMap.put("p1", p1);
		personMap.put("p2",p2);
		System.out.println(JSONArray.fromObject(personMap));
	}

}
