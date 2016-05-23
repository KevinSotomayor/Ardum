package persistence;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Util.Basics;
import Util.Constantes;
import Util.Date_solver;
import bussines.Actividad;
import bussines.Asignatura;
import bussines.Cuatrimestre;
import persistence.dao.ActividadDAO;
import persistence.dto.ActividadDTO;

public class ActividadDAOImp implements ActividadDAO {

protected ConnectionManager connectionManager;
	
	public ActividadDAOImp(){
		try{
			connectionManager= new ConnectionManager(Constantes.DATABASE);
		}catch(Exception e){
			System.err.println("Error en persistencia, ActividadDAOImp: "+e.getLocalizedMessage());
		}
	}
	
	@Override
	public ActividadDTO obtenerInformacionDeActividad(int id_actividad) {
		ActividadDTO actividadAux = null;
		try{
			connectionManager.connect();
			ResultSet actividadResultSet = connectionManager.queryDB("SELECT * from ACTIVIDAD where id_actividad = '"+id_actividad+"'");
			connectionManager.close();

			if (actividadResultSet.next()){
				actividadAux = new ActividadDTO(
						actividadResultSet.getInt("ID_ACTIVIDAD"),
						new AsignaturaDAOImp().obtenerInformacionAsignatura(actividadResultSet.getInt("ID_ASIGNATURA")).getTitulo(),
						actividadResultSet.getString("TITULO"),
						actividadResultSet.getString("DESCRIPCION"),
						Date_solver.convertirDateSQLEnLocalDateTime( actividadResultSet.getDate("FECHA_FINALIZACION")),
						actividadResultSet.getInt("TIEMPO_ESTIMADO"),
						actividadResultSet.getDouble("PORCENTAJE"),
						actividadResultSet.getInt("PRIORIDAD_U"),
						actividadResultSet.getInt("PRIORIDAD_T"),
						actividadResultSet.getBoolean("FINALIZADA"),
						actividadResultSet.getBoolean("PARA_DESPUES")
						);
			}
			System.out.println("Actividad recuperada con éxito, id: " + actividadAux.getId_actividad());
		}catch(Exception e){
			System.err.println("Ha ocurrido un error al buscar la actividad: "+e.getLocalizedMessage() );
		}
		return actividadAux;
	}
	

	@Override
	public ActividadDTO obtenerInformacionDeActividadExamen(int id_actividad_examen) {
		ActividadDTO actividadAux = null;
		try{
			connectionManager.connect();
			ResultSet actividadResultSet = connectionManager.queryDB("SELECT * from ACTIVIDAD A , EXAMEN E where A.id_actividad=E.id_actividad AND E.id_examen = '"+id_actividad_examen+"'");
			connectionManager.close();

			if (actividadResultSet.next()){
				actividadAux = new ActividadDTO(
						actividadResultSet.getInt("ID_ACTIVIDAD"),
						new AsignaturaDAOImp().obtenerInformacionAsignatura(actividadResultSet.getInt("ID_ASIGNATURA")).getTitulo(),
						actividadResultSet.getString("TITULO"),
						actividadResultSet.getString("DESCRIPCION"),
						Date_solver.convertirDateSQLEnLocalDateTime( actividadResultSet.getDate("FECHA_FINALIZACION")),
						actividadResultSet.getInt("TIEMPO_ESTIMADO"),
						actividadResultSet.getDouble("PORCENTAJE"),
						actividadResultSet.getInt("PRIORIDAD_U"),
						actividadResultSet.getInt("PRIORIDAD_T"),
						actividadResultSet.getBoolean("FINALIZADA"),
						actividadResultSet.getBoolean("PARA_DESPUES")
						);
			}
			System.out.println("Actividad_Examen recuperada con éxito, id: " + actividadAux.getId_actividad());
		}catch(Exception e){
			System.err.println("Ha ocurrido un error al buscar la actividad_examen: "+e.getLocalizedMessage() );
		}
		return actividadAux;
	}

	@Override
	public void eliminarActividad(int id_actividad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Actividad crearActividad(Actividad actividad) {
		Actividad actAux = actividad;
		try{
			connectionManager.connect();
			int id = crearSecuencia(Constantes.ACTIVIDAD_SQ);
			if(id>0){
				String str = "INSERT INTO ACTIVIDAD (ID_ACTIVIDAD, ID_ASIGNATURA, TITULO,"
						+ " DESCRIPCION, FECHA_FINALIZACION, FINALIZADA, "
						+ "TIEMPO_ESTIMADO, PORCENTAJE, PRIORIDAD_U, PRIORIDAD_T, PARA_DESPUES)"
						
						+" VALUES ("
						+id+","
						+actividad.getAsignatura().getId_asignatura()+",'"
					    +actividad.getTitulo()+"','"
					    +actividad.getDescripcion()+"','"
						+Date_solver.convertirLocalDateEnSQL(actividad.getFechafinalizacion())+"',"
						+actividad.isFinalizada()+","
						+actividad.getTiempoestimado()+","
						+actividad.getPorcentaje()+","
						+actividad.getPrioridadusuario()+","
						+actividad.getPrioridadtotal()+","
						+actividad.isPara_despues()
						+")";
						
				if(actAux!=null)
					actAux.setId_actividad(id);
				
				connectionManager.updateDB(str);
				System.out.println("\nActividad  creado con éxito: " + actAux);
			}
			connectionManager.close();
		}catch(Exception e){
			System.err.println("No se ha podido crear la actividad: " + e.getLocalizedMessage());
		}
		return actAux;
	}

	@Override
	public void editarActividad(Actividad actividad) {
		// TODO Auto-generated method stub
		
	}
	
	private int crearSecuencia(String nombreSecuencia){
		try{
		ResultSet sq = connectionManager.queryDB("CALL NEXT VALUE FOR " + nombreSecuencia);

		if (sq.next())
			return sq.getInt(1);

		}catch(Exception e){
		   System.err.println("Ha ocurrido un error al generar la secuencia de id "+e.getLocalizedMessage());
		}
		return -1;

	}

}
