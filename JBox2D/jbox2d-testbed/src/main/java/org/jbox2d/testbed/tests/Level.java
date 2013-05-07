package org.jbox2d.testbed.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.testbed.framework.TestbedTest;

public class Level extends TestbedTest {

	private float xMin;
	private float yMax;
	private float xRes;
	private float yRes;
	private float r;
	private String levelFile;
	private static float DENSITY=25;

	public Level(String fileName) {
		super();
		levelFile=fileName;
	}
	
	@Override
	public boolean isSaveLoadEnabled() {
		return true;
	}

	public void initTest(boolean argDeserialized) {
		if(argDeserialized){
			return;
		}

		getWorld().setGravity(new Vec2(0,0));

		initFromFile(levelFile);
	}

	private void createBody(Vec2 position, BodyType type, float charge) {
		//Make a circle
		CircleShape c2 = new CircleShape();
		c2.setRadius(r);
		//Make a fixture
		FixtureDef fd2 = new FixtureDef();   
		//Put the circle in the fixture and set density
		fd2.shape=c2;
		fd2.density = DENSITY;
		//Make the BodyDef, set its position, and set it as dynamic
		BodyDef bd2 = new BodyDef();
		bd2.position = position;
		bd2.type = type;
		//now create a Body in the world, and put the bodydef and the fixturedef into it
		Body body2 = getWorld().createBody(bd2);
		body2.createFixture(fd2);
		//set the charge to be negative
		body2.charge=charge;
	}

	private void initFromFile(String s) {
		Scanner scanner = null;
		try {
			File f = new File(s);
			System.out.println(f.getPath());
			scanner = new Scanner(f);
			System.out.println(scanner.next());
			xMin=scanner.nextFloat();
			System.out.println(scanner.next());
			yMax=scanner.nextFloat();
			System.out.println(scanner.next());
			xRes=scanner.nextFloat();
			System.out.println(scanner.next());
			yRes=scanner.nextFloat();
			System.out.println(scanner.next());
			r=scanner.nextFloat();
			String line=new String("");
			while (line.equals("") || line.charAt(0)=='\\') {
				line = scanner.nextLine();
				System.out.println(line);
			}
			int rowCount=0;
			do {
				for(int i=0; i<line.length(); i++) {

					//boolean putNew=false;
					Vec2 position = new Vec2(xMin + i * xRes, yMax- rowCount* yRes);
					System.out.println(position);

					switch (line.charAt(i)) {
					case '+':
						createBody(position, BodyType.STATIC, 1);
						//putNew=true;
						break;
					case '-':
						createBody(position, BodyType.STATIC, -1);
						//putNew=true;
						break;
					case 'o':
						createBody(position, BodyType.DYNAMIC, 1);
						//putNew=true;
						break;
					case '*':
						// TODO Draw a star! 
						//break;
					case ' ':
						//Do nothing
					}
				}
				line=scanner.nextLine();
				System.out.println(line);
				rowCount++;
			} while (line.charAt(0)!='\\');
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();
	}

	@Override
	public String getTestName() {
		return "Level "+levelFile;
	}
}
