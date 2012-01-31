package amcgala.framework.shape.util;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import amcgala.framework.math.Vector3d;
import amcgala.framework.shape.Polygon;

/**
 * Statischer PLY File Parser zum laden von Polygonen. Export von komplexen
 * Objekten aus Blender moeglich (siehe exsample Paket) <br/>
 * <br/>
 * <b>ACHTUNG!</b> : Export als Stanford (.ply) File ohne Normalen Vektoren !<br/>
 * Einstellungen lassen sich im Export Kontextmenue von zB Blender festlegen. <br/>
 * <i>Eigenschaft: "Normals"</i>
 * 
 * @author Steffen Troester
 * 
 */
public class PLYPolygonParser {

	/**
	 * Parst .ply File und gibt Polygone zurueck<br/>
	 * <br/>
	 * <b>ACHTUNG!</b> : Export als Stanford (.ply) File ohne Normalen Vektoren
	 * ! <br/>
	 * Einstellungen lassen sich im Export Kontextmenue von zB Blender
	 * festlegen. <br/>
	 * <i>Eigenschaft: "Normals"</i>
	 * 
	 * @param filePath
	 *            Pfad zur .ply Datei
	 * @param scale
	 *            Skalierung des Objektes (zB 100)
	 * @return ArrayList von allen Polygonen
	 * @throws IOException
	 */
	public static List<Polygon> parseAsPolygonList(String filePath, double scale)
			throws Exception {

		// resultierende Polygone
		ArrayList<Polygon> al = new ArrayList<Polygon>();
		// Koordinaten Anzahl
		int vertex_count = 0;
		// Polygone Anzahl
		int face_count = 0;
		// Koordinaten
		ArrayList<Vector3d> vertexes = new ArrayList<Vector3d>();
		// Stream öffnen (Exception!)
		InputStream inputStream = new FileInputStream(filePath);
		// Stream in Scanner legen
		final Scanner scanner = new Scanner(inputStream).useLocale(Locale.US);

		// Durchlauf des Dateiheaders
		while (scanner.hasNext() && scanner.hasNextLine()) {
			String currentLine = scanner.nextLine();
			// Lade Anzahl von Koordinaten
			if (currentLine.matches("element vertex [0-9]{1,5}")) {
				vertex_count = Integer.parseInt(currentLine.substring(15)
						.trim());
			}
			// Lade Anzahl von Polygonen
			if (currentLine.matches("element face [0-9]{1,5}")) {
				face_count = Integer.parseInt(currentLine.substring(12).trim());
			}
			// Next step wenn Dateiheader gelesen
			if (currentLine.matches("end_header")) {
				break;
			}
		}
		// Lade alle Koordinaten
		for (int i = 0; i < vertex_count; i++) {
			vertexes.add(new Vector3d(scanner.nextDouble() * scale, scanner
					.nextDouble() * scale, scanner.nextDouble() * scale));

		}

		// Lade alle Polygone
		for (int i = 0; i < face_count; i++) {
			int currentFace = scanner.nextInt();
			if (currentFace == 4) {
				al.add(new Polygon(vertexes.get(scanner.nextInt()), vertexes
						.get(scanner.nextInt()),
						vertexes.get(scanner.nextInt()), vertexes.get(scanner
								.nextInt())));
			} else if (currentFace == 3) {
				al.add(new Polygon(vertexes.get(scanner.nextInt()), vertexes
						.get(scanner.nextInt()),
						vertexes.get(scanner.nextInt())));
			} else {
				throw new IOException();

			}
		}
		/*
		 * TODO: Logger Anpassen !
		 */
		Logger log = Logger.getLogger(PLYPolygonParser.class.getName());
		log.log(Level.INFO, "loaded " + vertex_count + " vertexes and "
				+ face_count + " faces");
		return al;

	}
}
