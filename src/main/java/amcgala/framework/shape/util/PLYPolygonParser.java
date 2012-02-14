package amcgala.framework.shape.util;

/*
 * Copyright 2011 Cologne University of Applied Sciences Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import amcgala.framework.math.Vector3d;
import amcgala.framework.shape.Polygon;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Statischer PLY File Parser zum laden von Polygonen. Export von komplexen
 * Objekten aus Blender moeglich (siehe exsample Paket) <br/>
 * <br/>
 * 
 * @author Steffen Tröster
 * 
 */
public class PLYPolygonParser {

	private static final Logger log = LoggerFactory
			.getLogger(PLYPolygonParser.class);

	/**
	 * Parst .ply File und gibt Polygone zurueck<br/>
	 * <br/>
	 * 
	 * @param filePath
	 *            Pfad zur .ply Datei
	 * @param scale
	 *            Skalierung des Objektes (zB 100)
	 * 
	 * @return ArrayList von allen Polygonen
	 * 
	 * @throws IOException
	 */
	public static List<Polygon> parseAsPolygonList(String filePath, double scale)
			throws Exception {
		// Stream öffnen (Exception!)
		InputStream inputStream = new FileInputStream(filePath);
		return parseAsPolygonList(inputStream, scale);
	}

	/**
	 * Parst .ply File und gibt Polygone zurueck<br/>
	 * <br/>
	 * 
	 * @param inputStream
	 *            to File
	 * @param scale
	 *            Skalierung des Objektes (zB 100)
	 * 
	 * @return ArrayList von allen Polygonen
	 * 
	 * @throws IOException
	 */
	public static List<Polygon> parseAsPolygonList(InputStream inputStream,
			double scale) throws Exception {
		// resultierende Polygone
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
		// Koordinaten Anzahl
		int vertexCount = 0;
		// Polygone Anzahl
		int faceCount = 0;
		// Koordinaten
		ArrayList<Vector3d> vertices = new ArrayList<Vector3d>();
		// Stream in Scanner legen
		final Scanner scanner = new Scanner(inputStream).useLocale(Locale.US);

		// Durchlauf des Dateiheaders
		while (scanner.hasNext() && scanner.hasNextLine()) {
			String currentLine = scanner.nextLine();
			// Lade Anzahl von Koordinaten
			if (currentLine.matches("element vertex [0-9]{1,5}")) {
				vertexCount = Integer
						.parseInt(currentLine.substring(15).trim());
			}
			// Lade Anzahl von Polygonen
			if (currentLine.matches("element face [0-9]{1,5}")) {
				faceCount = Integer.parseInt(currentLine.substring(12).trim());
			}
			// Next step wenn Dateiheader gelesen
			if (currentLine.matches("end_header")) {
				break;
			}
		}
		// Lade alle Koordinaten
		for (int i = 0; i < vertexCount; i++) {
			vertices.add(new Vector3d(scanner.nextDouble() * scale, scanner
					.nextDouble() * scale, scanner.nextDouble() * scale));
			scanner.nextLine();
		}

		// Lade alle Polygone
		for (int i = 0; i < faceCount; i++) {
			int currentFace = scanner.nextInt();
			if (currentFace == 4) {
				polygons.add(new Polygon(vertices.get(scanner.nextInt()),
						vertices.get(scanner.nextInt()), vertices.get(scanner
								.nextInt()), vertices.get(scanner.nextInt())));
			} else if (currentFace == 3) {
				polygons.add(new Polygon(vertices.get(scanner.nextInt()),
						vertices.get(scanner.nextInt()), vertices.get(scanner
								.nextInt())));
			} else {
				throw new IOException();

			}
		}

		log.info("loaded " + vertexCount + " vertexes and " + faceCount
				+ " faces");
		return polygons;

	}
}
