package com.jspider.car_dekho.hibernate.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String fuel_type;
	private String company;
	private double price;
	@Override
	public String toString() {
		return  id + "\t" + name + "\t\t" + company + "\t\t" +  fuel_type+ "\t\t"
				+ price ;
	}
	
	
	
}
