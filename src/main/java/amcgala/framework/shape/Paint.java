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
package amcgala.framework.shape;

import amcgala.framework.camera.Camera;
import amcgala.framework.math.Matrix;
import amcgala.framework.math.Vector3d;
import amcgala.framework.renderer.Renderer;

/**
 * Zeichnet ein 2D/3D Shapeobjekt mit übergebenen Parametern in den Formen: BresenhamLine3d, BresenhamLine2d oder Vector3d <br /> 
 * Weiter Linien oder Vektoren können auch im nachhinein hinzugefügt werden.<br />
 * @author Sascha Lemke
 */
public class Paint extends Shape {

	private BresenhamLine3d linien [];
	private int position;
	public int length;
	
	/**
	 * Erstellt eine "leeres" Shapeobjekt.
	 */
	public Paint() {
		this.position = 0;
		this.linien = new BresenhamLine3d[10];
	}
	
	/**
	 * Wird benutzt um ein Shapeobjekt auf Basis eines Arrays zu entwerfen.
	 * @param koordinaten
	 */
	public Paint(Vector3d[] koordinaten) {
		if(koordinaten.length > 1) {
			this.linien = new BresenhamLine3d[koordinaten.length];
			for(int i = 0; i < koordinaten.length; i++) {
				if(i == koordinaten.length-1) {
					this.linien[i] = new BresenhamLine3d(koordinaten[i], new Vector3d(this.linien[0].x1, this.linien[0].y1, this.linien[0].z1));
				} else {
					this.linien[i] = new BresenhamLine3d(koordinaten[i], koordinaten[i+1]);
				}
			}
			this.position = this.linien.length-1;
		} else {
			this.linien = new BresenhamLine3d[10];
			this.linien[0] = new BresenhamLine3d(koordinaten[0], new Vector3d(0,0,0));
			this.position++;
		}
	}
	
	/**
	 * Übernimmt automatisch 2D-Linien um sie mit anderen Linien zu verbinden.
	 * @param linien
	 */
	public Paint(BresenhamLine2d[] linien) {
		this.linien = new BresenhamLine3d[10];
		if(linien.length > this.linien.length) {
			this.linien = newArray(this.linien);
		}
		for(int i = 0; i < linien.length; i++) {
			this.linien[i] = new BresenhamLine3d(new Vector3d(linien[i].x1, linien[i].y1, 0), new Vector3d(linien[i].x2, linien[i].y2, 0));
		}
		this.position = linien.length;
	}
	
	/**
	 * Übernimmt automatisch 3D-Linien um sie mit anderen Linien zu verbinden.
	 * @param linien
	 */
	public Paint(BresenhamLine3d[] linien) {
		this.linien = new BresenhamLine3d[10];
		if(linien.length > this.linien.length) {
			this.linien = newArray(this.linien);
		}
		for(int i = 0; i < linien.length; i++) {
			this.linien[i] = linien[i];
		}
		this.position = linien.length;
	}

	/**
	 * Fügt einen Punkt am Ende der Liste hinzu
	 * @param x
	 * @param y
	 */
	public void add(Vector3d point) {
		if(this.position >= this.linien.length - 1) {
			this.linien = newArray(this.linien);
		}
		this.linien[this.position+1] = new BresenhamLine3d(new Vector3d(this.linien[this.position].x2, this.linien[this.position].y2, this.linien[this.position].z2), point);
		this.position++;
	}
	
	/**
	 * Übernimmt die übergebenen Linien und bezieht sie in das ShapeObjekt mit ein.
	 * @param lines
	 */
	public void add(BresenhamLine3d[] lines) {
		if(this.position + lines.length >= this.linien.length) {
			this.linien = newArray(this.linien);
		}
		for(int i = 0; i < lines.length; i++) {
			this.linien[this.position] = lines[i];
			this.position++;
		}
	}
	
	/**
	 * Entfernt den letzten Eintrag.
	 * @return
	 */
	public void remove() {
		this.linien[this.position-1] = null;
		this.position--;
	}
	
	/**
	 * Entfernt einen Eintrag an gewählter Stelle.
	 * @param index Die Stelle des Knotens der gelöscht werden soll (Achtung! Wir zählen von 0 an!)
	 * @return
	 */
	public void remove(int index) {
		if(checkIndex(index)) {
			for(int i = index; i < this.position-1; i++) {
				this.linien[index] = this.linien[index+1];
			}
			this.linien[this.position-1] = null;
			this.position--;
		}
	}
	
	/**
	 * Erweitert das Array um die doppelte Groesse, wenn der Platz aufgebraucht ist.
	 */
	private BresenhamLine3d[] newArray(BresenhamLine3d[] oldArray) {
		BresenhamLine3d[] newArray = new BresenhamLine3d[oldArray.length*2];
		for(int i = 0; i < oldArray.length; i++) {
			newArray[i] = oldArray[i];
		}
		return newArray; 
	}
	
	/**
	 * Überprüft den Index.
	 * @param index
	 * @return
	 */
	private boolean checkIndex(int index) {
		if(this.linien[index] != null) return true;
		else return false;
	}
	
	/**
	 * Überschreibt die <i>toString</i> Methode der Klasse <i>Object</i> und gibt bei Aufruf die Linien mit den Koordinaten wieder, sowie die Anzahl der Linen und ob diese Inhalt besitzen.
	 */
	@Override
	public String toString() {
		String ausgabe = "";
		for(int i = 0; i < this.linien.length && this.linien[i] != null; i++) {
			ausgabe += "" + i + ". Linie: \n";
			ausgabe += "\t x1: " + this.linien[i].x1 + "\n";
			ausgabe += "\t y1: " + this.linien[i].y1 + "\n";
			ausgabe += "\t z1: " + this.linien[i].z1 + "\n\n";
			
			ausgabe += "\t x2: " + this.linien[i].x2 + "\n";
			ausgabe += "\t y2: " + this.linien[i].y2 + "\n";
			ausgabe += "\t z2: " + this.linien[i].z2 + "\n";
		}
		ausgabe += "- Linien insgesamt: " + this.linien.length;
		int ll = 0;
		for(int i = 0; i < this.linien.length; i++) {
			if(this.linien[i] == null) ll++;
		}
		ausgabe += "\n- Leere Linien insgesamt: " + ll;
		ausgabe += "\n- Aktuelle Position: " + this.position;
		return ausgabe;
	}
	

	@Override
	public void render(Matrix transformation, Camera camera, Renderer renderer) {
		for(int i = 0; i < this.linien.length && this.linien[i] != null; i++) {
			this.linien[i].render(transformation, camera, renderer);
		}
	}
}
