package gui.controller;

import java.io.IOException;

import bussines.Asignatura;
import bussines.Curso;
import bussines.Unidad_Logica;
import bussines.Universidad;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/*
 * Clase principal que lanza la aplicacion, la cual lanzara las demas pantallas de la aplicacion
 * Lanza la pantalla con el border pane contenedor de el menu lateral.
 * 
 * Esta clase comprobara al inicio si el usuario ya ha preconfigurado la aplicacion para lanzar 
 * una pantalla ( la de las actividades ) u otra ( la de configuracion de inicio )
 * 
 */

public class MainController  extends Application {
	private BorderPane root;
	private BorderPane userRoot;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			if(Unidad_Logica.getInstance().hayUsuario()){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/MenuLateralBorderPanePrincipal.fxml"));
			root = (BorderPane)loader.load();
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle(Util.Constantes.ARDUM);
			
			//acceso al controlador de la pantalla de activiades con menu lateral
			//previo paso de datos antes de que se lanze la pantalla
			MenuLateralController pantallaInicialActiviades = loader.<MenuLateralController>getController();
			pantallaInicialActiviades.initStage(primaryStage, this);
			
			primaryStage.show();
			
			//No barra inferior, no gain.
			}
			else{inicioUserRoot(primaryStage);}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//la pantalla de activiades siempre abre la misma, solo que con diferentes asignaturas que tendrán activiades diferentes
	public void abrirPantallaActividades(Stage primaryStage, Asignatura asignatura){
		primaryStage.setTitle(Util.Constantes.ARDUM + " - " + asignatura.getTitulo());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/ListaActividades.fxml"));
			GridPane pantallaListaActiviades = (GridPane)loader.load();
			
			if(root!=null){
				root.setCenter(pantallaListaActiviades);
			}
			
			ListaActividadesController controladorPantalla = loader.<ListaActividadesController>getController();
			controladorPantalla.initStage(primaryStage, this, asignatura);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error al abrir la pantalla de activiades de una asignatura: " + e.getLocalizedMessage());
		}
		
	}
	public void inicioUserRoot(Stage secondaryStage){
		 try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/UsuarioRootLayout.fxml"));
			userRoot = (BorderPane)loader.load();
			Scene scene = new Scene(userRoot);
			secondaryStage.setScene(scene);
			secondaryStage.setTitle(Util.Constantes.ARDUM);
			
			UsuarioRootController userRootController = loader.<UsuarioRootController>getController();
			userRootController.initStage(secondaryStage, this);
			
			secondaryStage.show();
			inicioUsuario(secondaryStage);
		} catch (IOException e){
			// TODO Auto-generated catch block
			System.err.println("Error Root Layout Usuario " + e.getLocalizedMessage());
		}}
	public void inicioUsuario(Stage secondaryStage){
		 try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/InicioUsuario.fxml"));
			GridPane initUser = (GridPane)loader.load();
			
			if(userRoot!=null){
				userRoot.setCenter(initUser);
			}
			
			InicioUsuarioController inicioU = loader.<InicioUsuarioController>getController();
			inicioU.initStage(secondaryStage, this);
			
		} catch (IOException e){
			// TODO Auto-generated catch block
			System.err.println("Error al establecer la barra inferior " + e.getLocalizedMessage());
		}
	}
	public void inicioUniversidadGrado(Stage secondaryStage,Universidad upv){
		 try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/InicioUniversidadGrado.fxml"));
			GridPane initUser = (GridPane)loader.load();
			
			if(userRoot!=null){
				userRoot.setCenter(initUser);
			}
			
			InicioUniversidadGradoController inicioU = loader.<InicioUniversidadGradoController>getController();
			inicioU.initStage(secondaryStage, this, upv);
			
		} catch (IOException e){
			// TODO Auto-generated catch block
			System.err.println("Error al establecer la segunda pantalla del Inicio " + e.getLocalizedMessage());
		}
	}
	public void inicioCuatrimestreAsignatura(Stage secondaryStage, Curso actual){
		 try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/InicioCuatrimestreAsignatura.fxml"));
			GridPane initUser = (GridPane)loader.load();
			
			if(userRoot!=null){
				userRoot.setCenter(initUser);
			}
			
			InicioCuatrimestreAsignaturaController inicioU = loader.<InicioCuatrimestreAsignaturaController>getController();
			inicioU.initStage(secondaryStage, this, actual);
			
		} catch (IOException e){
			// TODO Auto-generated catch block
			System.err.println("Error al establecer la segunda pantalla del Inicio " + e.getLocalizedMessage());
		}
	}
	
	public void mostrarListaDeNotificaciones(Stage secondaryStage, boolean mostrar){
		
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/ListaNotificaciones.fxml"));
			AnchorPane initUser = (AnchorPane)loader.load();
			
			if(root!=null && mostrar){
				root.setRight(initUser);
			}else if(root!=null){
				root.setRight(null);
			}
			
			ListaNotificacionesController listaNotificacionesController = loader.<ListaNotificacionesController>getController();
			listaNotificacionesController.initStage(secondaryStage, this);
			
		} catch (IOException e){
			// TODO Auto-generated catch block
			System.err.println("Error al establecer la segunda pantalla del Inicio " + e.getLocalizedMessage());
		}
	}

	
	
	
}
