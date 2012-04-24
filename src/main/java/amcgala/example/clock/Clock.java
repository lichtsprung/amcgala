/*
* Copyright 2011 Cologne University of Applied Sciences Licensed under the
* Educational Community License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may
* obtain a copy of the License at
*
* http://www.osedu.org/licenses/ECL-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
* or implied. See the License for the specific language governing
* permissions and limitations under the License.
*/
package amcgala.example.clock;

import java.text.SimpleDateFormat;
import java.util.Date;

import amcgala.framework.animation.Animation;
import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;
import amcgala.framework.shape.Polygon;
import amcgala.framework.shape.Shape;
import amcgala.framework.shape3d.Circle;

public class Clock extends Shape {
	
	private Polygon second, minute, hour;
	private Circle clockFace;
	
	public Clock() {
		this.clockFace = new Circle(0, 0, 1, 120);
		second = new Polygon(new Vector3d(-5, 0, 0), new Vector3d(5, 0, 0),
		new Vector3d(0, 5, 0), new Vector3d(0, 0, 0));
		minute = new Polygon(new Vector3d(-5, 0, 0), new Vector3d(5, 0, 0),
		new Vector3d(0, 5, 0), new Vector3d(0, 0, 0));
		hour = new Polygon(new Vector3d(-5, 0, 0), new Vector3d(5, 0, 0),
		new Vector3d(0, 5, 0), new Vector3d(0, 0, 0));
		
		this.setAnimation(new ClockAnimation());
	
	}
	
	@Override
	public void render(Matrix arg0, Camera arg1, Renderer arg2) {
		clockFace.render(arg0, arg1, arg2);
		second.render(arg0, arg1, arg2);
		minute.render(arg0, arg1, arg2);
		hour.render(arg0, arg1, arg2);
	}
	
	private class ClockAnimation extends Animation<Shape> {
	
	@Override
	public void animate() {
		Date dt = new Date();
		SimpleDateFormat microFormat = new SimpleDateFormat("S");
		SimpleDateFormat secondFormat = new SimpleDateFormat("ss");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		
		double secondDegree = 360 * ((Double.parseDouble(secondFormat.format(dt))+Double.parseDouble(microFormat.format(dt))/1000.0) / 60.0);
		double minuteDegree = 360 * ((Double.parseDouble(minuteFormat.format(dt)) + Double.parseDouble(secondFormat.format(dt)) / 60.0) / 60.0);
		double hourDegree = 360 * ((Double.parseDouble(hourFormat.format(dt)) + Double.parseDouble(minuteFormat.format(dt)) / 60.0) / 12.0);
		
		double secondRadian = Math.toRadians(secondDegree);
		double minuteRadian = Math.toRadians(minuteDegree);
		double hourRadian = Math.toRadians(hourDegree);
		
		second = new Polygon(
		new Vector3d(5 * Math.sin(secondRadian - Math.PI / 2),
		5 * Math.cos(secondRadian - Math.PI / 2), 0),
		new Vector3d(10 * Math.sin(secondRadian + Math.PI),
		10 * Math.cos(secondRadian + Math.PI), 0),
		new Vector3d(5 * Math.sin(secondRadian + Math.PI / 2),
		5 * Math.cos(secondRadian + Math.PI / 2), 0),
		new Vector3d(100 * Math.sin(secondRadian), 100 * Math
		.cos(secondRadian), 0), new Vector3d(0, 0, 0));
		minute = new Polygon(
		new Vector3d(5 * Math.sin(minuteRadian - Math.PI / 2),
		5 * Math.cos(minuteRadian - Math.PI / 2), 0),
		new Vector3d(10 * Math.sin(minuteRadian + Math.PI),
		10 * Math.cos(minuteRadian + Math.PI), 0),
		new Vector3d(5 * Math.sin(minuteRadian + Math.PI / 2),
		5 * Math.cos(minuteRadian + Math.PI / 2), 0),
		new Vector3d(100 * Math.sin(minuteRadian), 100 * Math
		.cos(minuteRadian), 0), new Vector3d(0, 0, 0));
		hour = new Polygon(
		new Vector3d(5 * Math.sin(hourRadian - Math.PI / 2),
		5 * Math.cos(hourRadian - Math.PI / 2), 0),
		new Vector3d(10 * Math.sin(hourRadian + Math.PI),
		10 * Math.cos(hourRadian + Math.PI), 0),
		new Vector3d(5 * Math.sin(hourRadian + Math.PI / 2),
		5 * Math.cos(hourRadian + Math.PI / 2), 0),
		new Vector3d(50 * Math.sin(hourRadian), 50 * Math
		.cos(hourRadian), 0), new Vector3d(0, 0, 0));
	}
	
	}

}