package gui.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.control.PopOver;

import com.sun.javafx.geom.Shape;

import Util.Basics;
import bussines.Actividad;
import bussines.Asignatura;
import bussines.Clase;
import bussines.Unidad_Logica;
import bussines.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


/*
 * author @kevin
 * 
 * Clase del controlador de la pantalla donde sale el menu lateral y el centro de la aplicacion, el cual se ira
 * sustituyendo su contenido dependiendo de la Asigntara que den click para mostrar las actividades
 */

public class MenuLateralController implements Initializable {
	//Objetos basicos de paso entre pantallas: Stage y Controlador principal
	private Stage primaryStage;
	private MainController controladorPrincipal;
	private Unidad_Logica fachada;
	
	//pantalla principal si no hay tareas
	@FXML private Text tvMensajeTodoOk;
	
	//Etiquetas XML para identificar objetos en pantalla
	//Barra TOP
	@FXML private ImageView ivAvatar;
    @FXML private Circle circleAvatar;
	@FXML private Text tvNombreUsuario;
	@FXML private Text logoArdum;
	@FXML private ImageView ivNotificaciones;
	@FXML private ImageView ivNotificacionTriangulo;
	@FXML private ImageView ivConfiguracion;

	//Botones principales laterales
	@FXML private Text tvBandeja;
	@FXML private HBox hbBandeja;
	@FXML private Text tvHoy;
	@FXML private HBox hbHoy;
	@FXML private Text tvParaDespues;
	@FXML private HBox hbParaDespues;
	
	//menu lateral
	@FXML private Text TextTituloGrado;
	@FXML private ListView listViewAsignaturas;
	@FXML private ImageView ivAnyadirAsignatura;
	
	//boolean para mostrar o no la lista de notificaciones
	private boolean mostrarNotificaciones;
	
	private Usuario usuario;
	
	public void initStage(Stage stage, MainController controladorPrincipal){
		this.primaryStage = stage;
		this.controladorPrincipal = controladorPrincipal;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.fachada = Unidad_Logica.getInstance();
		this.usuario = fachada.informacionUsuario(1);
		
		inicializarContenidoVisual();
		
		//prueba para rellenar la lista de asignaturas y mostrarlas en pantalla
		List<Asignatura> listaAsignaturas = new ArrayList<Asignatura>();
		listaAsignaturas.add(new Asignatura("CSD", Color.CRIMSON));
		listaAsignaturas.add(new Asignatura("ETC",Color.AQUAMARINE ));
		listaAsignaturas.add(new Asignatura("PSW",Color.BLUEVIOLET));
		listaAsignaturas.add(new Asignatura("DDS",Color.CADETBLUE));
		listaAsignaturas.add(new Asignatura("GPR",Color.CORAL));
		
		Asignatura asignaturaPruebaConActividades = new Asignatura("MFI", Color.BLANCHEDALMOND);
		
		//Actividades de prueba
		Actividad actPrueba1 = new Clase(1,asignaturaPruebaConActividades, "Avanzar con la presentación", "Descripcion de prueba pendiente", LocalDateTime.now(), 0, 0, 0, true, false);
		actPrueba1.setId_actividad(1);
		Actividad actPrueba2 = new Clase(2,asignaturaPruebaConActividades, "Hacer actividad de Poliformat", "Descripcion de prueba pendiente", LocalDateTime.now(), 0, 0, 0, true, false);
		actPrueba2.setId_actividad(2);
		Actividad actPrueba3 = new Clase(3,asignaturaPruebaConActividades, "Adelantar trabajo de la semana que viene", "Descripcion de prueba pendiente", LocalDateTime.now(), 0, 0, 0, true, false);
		actPrueba3.setId_actividad(3);
		Actividad actPrueba4 = new Clase(4,asignaturaPruebaConActividades, "Actividad pendiente de hacer del otro día", "Descripcion de prueba pendiente", LocalDateTime.now(), 0, 0, 0, true, false);
		actPrueba4.setId_actividad(4);
		
		asignaturaPruebaConActividades.anyadirActividad(actPrueba1);
		asignaturaPruebaConActividades.anyadirActividad(actPrueba2);
		asignaturaPruebaConActividades.anyadirActividad(actPrueba3);
		asignaturaPruebaConActividades.anyadirActividad(actPrueba4);

		
		listaAsignaturas.add(asignaturaPruebaConActividades);
		
		ObservableList<Asignatura> loAsignaturas = FXCollections.observableArrayList(listaAsignaturas);
		listViewAsignaturas.setItems(loAsignaturas);
		
		if(fachada.obtenerInformacionGrado(1)!=null){
		    TextTituloGrado.setText(fachada.obtenerInformacionGrado(1).getTitulacion());
		    TextTituloGrado.setFont(Basics.generateFontById(8, 16));
		}
		
		//lista de asignaturas chetada + estilo en css
		listViewAsignaturas.getStylesheets().add(getClass().getResource("/gui/view/application.css").toExternalForm());
		listViewAsignaturas.setCellFactory(c-> new ListCellFactoryAsignaturas());

		
		/* Método escuchador de la lista para cuando se seleccione un elemento
		 * se pueda lanzar la pantalla con las activiades correspondientes a esa asignatura
		 */
		listViewAsignaturas.setOnMouseClicked(new EventHandler<Event>(){

			@Override
			public void handle(Event event) {
				if(listViewAsignaturas.getSelectionModel().getSelectedItem()!=null){
					lanzarPantallaDeActividades(listaAsignaturas.get(listViewAsignaturas.getSelectionModel().getSelectedIndex()));
				}
			}
			
		});
		
		
	}
	public void inicializarContenidoVisual(){
	    logoArdum.setFont(Basics.generateFontById(23, 35));

		//nombre de usuario y fuente
		if(usuario!=null){
			tvNombreUsuario.setText(usuario.getNombreCompleto());
			tvNombreUsuario.setFont(Basics.generateFontById(1, 14));
			
			//establecer imagen del item seleccionado en la pantalla de descripcion
			BufferedImage bufferedImage = null;
			try {
				bufferedImage = ImageIO.read(new File(usuario.getAvatar()));
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(bufferedImage!=null){
					Image image = SwingFXUtils.toFXImage(bufferedImage, null);
					ivAvatar.setImage(image);
					circleAvatar.setFill(new ImagePattern(image));
				}
			}
		}
		
		tvMensajeTodoOk.setFont(Basics.generateFontById(9, 23));
		tvBandeja.setFont(Basics.generateFontById(3, 14));
		hbBandeja.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				tvBandeja.setFont(Basics.generateFontById(1, 14));
				hbBandeja.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				hbHoy.setBackground(new Background(new BackgroundFill(Color.web("#f3f3f3"), null, null)));
				tvHoy.setFont(Basics.generateFontById(3, 14));
				hbParaDespues.setBackground(new Background(new BackgroundFill(Color.web("#f3f3f3"), null, null)));
				tvParaDespues.setFont(Basics.generateFontById(3, 14));

				//acciones al hacer click
				
			}
		});
		
		tvHoy.setFont(Basics.generateFontById(3, 14));
		hbHoy.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				tvHoy.setFont(Basics.generateFontById(1, 14));
				hbHoy.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				hbBandeja.setBackground(new Background(new BackgroundFill(Color.web("#f3f3f3"), null, null)));
				tvBandeja.setFont(Basics.generateFontById(3, 14));

				hbParaDespues.setBackground(new Background(new BackgroundFill(Color.web("#f3f3f3"), null, null)));
				tvParaDespues.setFont(Basics.generateFontById(3, 14));
				//acciones al hacer click
				
			}
		});
		
		tvParaDespues.setFont(Basics.generateFontById(3, 14));
		hbParaDespues.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				tvParaDespues.setFont(Basics.generateFontById(1, 14));
				hbParaDespues.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				hbHoy.setBackground(new Background(new BackgroundFill(Color.web("#f3f3f3"), null, null)));
				tvHoy.setFont(Basics.generateFontById(3, 14));
				hbBandeja.setBackground(new Background(new BackgroundFill(Color.web("#f3f3f3"), null, null)));
				tvBandeja.setFont(Basics.generateFontById(3, 14));

				//acciones al hacer click
				
			}
		});
				
		ivNotificaciones.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if(!mostrarNotificaciones){
					mostrarNotificaciones = true;
					lanzarPantallaDeNotificaciones(mostrarNotificaciones);
				}else{
					mostrarNotificaciones = false;
					lanzarPantallaDeNotificaciones(mostrarNotificaciones);
				}
				ivNotificacionTriangulo.setVisible(mostrarNotificaciones);
			}
		});

	}
	
	public void lanzarPantallaDeActividades(Asignatura asignaturaSeleccionada){
		controladorPrincipal.abrirPantallaActividades(primaryStage, asignaturaSeleccionada);
	}
	public void lanzarPantallaDeNotificaciones(boolean mostrar){
		controladorPrincipal.mostrarListaDeNotificaciones(primaryStage, mostrar);
	}

}
