package com.jspider.car_dekho.hibernate.dao;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.jspider.car_dekho.hibernate.dto.Car;

public class CarDekhoDAO {
	
	List<Car>Temp;
	//==================DATABASE==================
	private   EntityManagerFactory entityManagerFactory;
	private   EntityManager entityManager;
	private  EntityTransaction entityTransaction;
	private  Query query;
	private  Scanner sc = new Scanner(System.in);;
	
	public void closeScanner()
	{
		sc.close();
	}
	private  void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory("Cardekho");
		entityManager = entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
		
	}
	
	private  void closeConnection() {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
		if (entityManager != null) {
			entityManager.close();
		}
		
		if (entityTransaction != null) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		}
		
	}
	
	//------------------Get User Input Choice Code ------------------
		 
		public  int get_Choice()
		{
			
			System.out.print("\nEnter Your Choice : ");
			int s=0;
			int Cnum=0;
			try {
				Cnum= sc.nextInt();
				
			} catch (InputMismatchException e) {
				System.out.println("\n#############--Enter Only Integer Value !--############ \n Try Again ! ");
				s++;
			}
			
			while (s!=0) 
			{
				get_Choice();
			}
			
			return Cnum;
		
		}
		
		//==========REMOVE CAR==========
		public void Remove_Car() {
			System.out.println("\nRemove Car By : ");
			int m = searchCarBy();
			
			if (!Temp.isEmpty()) {
				
				
				try {
					openConnection();
					entityTransaction.begin();

					for(Car car : Temp)
					{
						entityManager.remove(entityManager.find(Car.class, car.getId()));
					}
				}catch (Exception e) {
					e.printStackTrace();
				} 
				finally {
					entityTransaction.commit();
					closeConnection();
				}
				
				System.out.println("\nSuccesfully Deleted Data ! ");
			} 
			else if(Temp.isEmpty() && m<6 ) {
				System.out.println("Sorry Data Is Not Found ! Try Again Another Way  ");
				Remove_Car();
			}
			Temp.clear();
			
		}
		
		//=============	Add New Car ============
		public void Add_Car() {
			
			Car AdCar = new Car();
			openConnection();
			entityTransaction.begin();
			try {
				System.out.print("Enter Your Car Name               : ");
				AdCar.setName(sc.next().toUpperCase());
				System.out.print("Enter Your Car Company Name(Brand): ");
				AdCar.setCompany(sc.next().toUpperCase());
				System.out.print("Enter Car Fuel Type               : ");
				AdCar.setFuel_type(sc.next().toUpperCase());
				System.out.print("Enter Car price                   : ");
				AdCar.setPrice(sc.nextDouble());
				entityManager.persist(AdCar);
				entityTransaction.commit();
				System.out.println("\n---------------"+AdCar.getName() + " Added SucessFully In DataBase --------------");
				
			} catch (InputMismatchException e) {
				System.out.println("\n####### Wrong Data Enterd Please ! #######");
				System.out.println("\n******* Id & Price Take Only Integer Value ******* ");
				Add_Car();
			} finally {
				closeConnection();
		}
			
		}
		public void displayAllDetails() {
			
			openConnection();
			query = entityManager.createQuery("Select cars from Car cars");
			Temp = query.getResultList();
			displayBPlate();
			for (Car car : Temp) {
				System.out.println(car);
			}
			System.out.println("======================================================================");
			Temp.clear();
			closeConnection();
		}
		
		public  void displayBPlate() {
			System.out.println("\nID\t"+"Car_Name\t"+"Comp_Name\t"+"Fuel_Type\t"+"Price ");
			System.out.println("======================================================================");
		}
		
		public void searchCar() {
			System.out.print(	"\nSEARCH CAR BY : \n");
			
			int num=searchCarBy(); 	
			
			if (num<6 && !Temp.isEmpty()) {
				displayBPlate();
				for (Car car : Temp)
				{
					System.out.println(car);;
				}
				Temp.clear();
			}
			else if(num==6)
			{
				return;
			}
			else if(Temp.isEmpty()&& num!=6)
			{
				System.out.println("\nSorry Data Not Found Try Again !");
				searchCar();
			}
			
		}
		public int searchCarBy()
		{
			openConnection();
			System.out.print(
				    "\n1.CAR ID  \n"+
					"2.CAR NAME \n"+
				    "3.Company Name \n"+
					"4.Fuel Type \n"+
				    "5.Price\n"+
					"6.Back To Main Menu\n");
			int k = get_Choice();
			
			switch (k) {
			case 1:
					{
						System.out.print("\nEnter Car Id :");
						int Cid= sc.nextInt();
						Car car = entityManager.find(Car.class, Cid);
						Temp.add(car);
						break;
					}
			case 2:
					{
						System.out.print("\nEnter Car Name :");
						String Cname= sc.next();
						query = entityManager.createQuery("select car from Car car where name = :n");
						query.setParameter("n", Cname.toUpperCase());
						
						Temp.addAll(query.getResultList());
						break;
					}
			case 3:
					{
						System.out.print("\nEnter Company Name :");
						String Cname= sc.next();
						query = entityManager.createQuery("select car from Car car where company = :n");
						query.setParameter("n", Cname.toUpperCase());
						Temp.addAll(query.getResultList());
						break;
					}
			case 4:
					{
						System.out.print("\nEnter Fuel Type  :");
						String Ftype= sc.next();
						query = entityManager.createQuery("select car from Car car where fuel_type = :n");
						query.setParameter("n", Ftype.toUpperCase());
						Temp.addAll(query.getResultList());
						break;
					}
			case 5:
					{
						System.out.print("\nEnter Price :");
						double Cpri= sc.nextInt();
						query = entityManager.createQuery("select car from Car car where price = :n");
						query.setParameter("n", Cpri);
						Temp.addAll(query.getResultList());
						break;
					}
			case 6 :
					{
						break;
					}
			default:
					{
						System.out.println("\nWrong Choice Try Again ! ");
						searchCarBy();
						break;
					}
			}
			closeConnection();
			return k;
		}
		public void updateCarDetails() {
			displayAllDetails();
			System.out.print("\nUpdate Data By :  ");
			int a= searchCarBy(); 
			
			if (Temp.isEmpty() && a!=6) {
				System.out.println("\nData Not Found Please  try Again !");
				updateCarDetails();
			}
			else if(a==6)
			{
				return;
			}
			else if(a!=6) {
				displayBPlate();
				for(Car Ddata : Temp)
				{
					System.out.println(Ddata);
				}
				System.out.println("\nWhat Do You Want To Update ! ");
				System.out.println("1. Car Name \n2. Company Name \n3. Fuel Type \n4. Price");
				a = get_Choice();
			}
			
			try {
				
				if (a<6) {
					openConnection();
					entityTransaction.begin();
					int res=0;
					switch(a)
					{
				
					case 1 :{
						System.out.print("\nEnter Your Updated Value for Car Name : " );
						String Cname = sc.next();
						query = entityManager.createQuery("update Car set name = '"+Cname+"' where id = :i");
						for (Car car : Temp) {
							Car c1 = entityManager.find(Car.class, car.getId());
							c1.setName(Cname.toUpperCase());
							entityManager.persist(c1);
							res++;
						}
						System.out.println(res+"row(s) affected ");
						break;
					}
					case 2 :{
						System.out.print("\nEnter Your Updated Value for Company Name: " );
						String Cname = sc.next();
						
						for (Car car : Temp) {
							Car c1 = entityManager.find(Car.class, car.getId());
							c1.setCompany(Cname.toUpperCase());
							entityManager.persist(c1);
							res++;
						}
						System.out.println(res+"row(s) affected ");
						break;
					}
					case 3 :{
						System.out.print("\nEnter Your Updated Value for Fuel Type : " );
						String Cname = sc.next();
						
						for (Car car : Temp) {
						Car c1 = entityManager.find(Car.class, car.getId());
						c1.setFuel_type(Cname.toUpperCase());
						entityManager.persist(c1);
						}
						break;
					}
					case 4 :{
						System.out.print("\nEnter Your Updated Value for Price : " );
						double num = sc.nextDouble();
						
						for (Car car : Temp) {
							Car c1 = entityManager.find(Car.class, car.getId());
							c1.setPrice(num);
							entityManager.persist(c1);
							}
						System.out.println(res+"row(s) affected ");
						break;
					}
					
					default:{
						break;
						}
					
					}
				entityTransaction.commit();
				System.out.println("\n=======Your value Will Be Updated successfully !========");
				
				
				Temp.clear();
				}
				else {
					System.out.println("Wrong Choice Try Again ! ");
					updateCarDetails();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				closeConnection();
			}			
		}

		

		

}
