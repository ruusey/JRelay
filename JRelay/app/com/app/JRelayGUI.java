package com.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.data.GameData;
import com.event.PluginMetaData;
import com.models.Packet;
import com.models.PacketMeta;
import com.models.Server;
import com.relay.JRelay;
import com.relay.User;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import plugins.ReconnectHandler;

public class JRelayGUI extends Application {
    public static final int APP_WIDTH = 550;
    public static final int APP_HEIGHT = 400;
    public static Label proxyStatus;
    public static TextArea console;
    public static TableView connections;
    public static TableView plugins;
    public static TableView<TabPane> pluginMeta;
    public static boolean relayStarted = false;
    public static ArrayList<TextField> settings;
    public static Stage app;
    
    public static float anPercent = 0.25f;
    public static int starFiler = 10;
    public static String DEFAULT_SERVER = "54.183.179.205";
    public static boolean savedLog = false;

    public static void main(String[] args) {
	launch(args);
	

    }

    @Override
    public void start(Stage primaryStage) {

	JRelayGUI.app = primaryStage;
	primaryStage.getIcons().add(new Image("icon.png"));
	primaryStage.setTitle("JRelay - RotMG Proxy");
	primaryStage.setResizable(false);

	Group root = new Group();
	Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT, Color.WHITE);
	scene.getStylesheets().addAll("app.css");
	ImageView imv = new ImageView();
	Image logo = new Image("icon.png");
	imv.setImage(logo);
	imv.setFitWidth(70);
	imv.setPreserveRatio(true);
	imv.setSmooth(true);
	imv.setCache(true);

	HBox hb = new HBox();

	Label header = createLabel("JRelay", 26);
	header.setPadding(new Insets(16, 0, 0, 5));
	hb.setPadding(new Insets(14, 0, 0, 10));
	hb.getChildren().addAll(imv, header);

	BorderPane componentLayout = new BorderPane();

	TabPane tabs = buildTabs();
	tabs.getTabs().get(0).setContent(buildConsole());
	if (!GameData.loadData()) {
	    JRelay.info("GameData unable to load. Exiting...");
	    System.exit(0);
	}
	
	tabs.getTabs().get(2).setGraphic(createLabel("Servers", 16));
	tabs.getTabs().get(2).setContent(buildServerBox());
	componentLayout.setTop(hb);
	componentLayout.setCenter(tabs);
	//startPluginUpdate();

	root.getChildren().add(componentLayout);
	// Add the Scene to the Stage
	primaryStage.setScene(scene);
	primaryStage.show();
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(WindowEvent event) {
		JRelay.instance.shutdown();
		Platform.exit();
        System.exit(0);
	    }
	});
	log("JRelay for RotMG " + JRelay.GAME_VERSION);
//	Thread td = new Thread(new ResourceMonitor());
//	td.start();
	
    }

    public static void warn(String s) {
	try {
	    printLog("[WARN] " + s + "\n");
	} catch (Exception e) {
	    JRelay.error(e.getMessage());
	}
    }

    public static void log(String s) {
	try {
	    printLog("[INFO] " + s + "\n");
	} catch (Exception e) {
	    JRelay.error(e.getMessage());
	}
    }

    public static void error(String s) {
	try {

	    printLog("[SEVERE] " + s + "\n");
	} catch (Exception e) {
	    JRelay.error(e.getMessage());

	}
    }

    public static void printLog(String s) throws IOException {
	Platform.runLater(new Runnable() {
	    public void run() {
		console.appendText(s);
	    }
	});
    }

    private Button createButton(String string, int size) {
	Button text = new Button(string);
	text.setStyle("-fx-font-family: \"arial\";" + "-fx-font-size: " + size + "px;");

	return text;
    }

    public VBox buildConsole() {
	final VBox consolePane = new VBox();
	consolePane.setPadding(new Insets(10, 0, 0, 1));
	final HBox consoleOptions = new HBox();

	Separator separator1 = new Separator();
	separator1.setOrientation(Orientation.HORIZONTAL);
	Separator separator2 = new Separator();
	separator2.setOrientation(Orientation.HORIZONTAL);

	Button start = createButton("Start Proxy", 14);
	hookProxyControl(start);
	Button saveCon = createButton("Save Log", 14);
	hookSaveConsole(saveCon);
	Button clearCon = createButton("Clear Console", 14);
	hookClearConsole(clearCon);
	consoleOptions.getChildren().addAll(start, separator1, saveCon, separator2, clearCon);
	consoleOptions.setPadding(new Insets(0, 0, 5, 0));
	// CONSOLE TEXT AREA
	TextArea con = new TextArea();
	con.setEditable(false);
	con.setPrefWidth(APP_WIDTH / 2);
	con.setPrefHeight(APP_HEIGHT / 2);
	con.setWrapText(false);
	Label status = createLabel("Not Running", 16);
	proxyStatus = status;
	console = con;
	consolePane.getChildren().addAll(consoleOptions, con, status);
	return consolePane;
    }

    public void hookSaveConsole(Button b) {
	b.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		saveConsole();
		createPopup("Saved Console Log", "Ok");

	    }
	});
    }

    public void saveConsole() {
	String pattern = "yyyy-MM-dd HH-mm-ss";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	String date = simpleDateFormat.format(new Date());
	try {
	    Path file = Paths.get(JRelay.RES_LOC + "logs//console_log" + date + ".txt");

	    Files.write(file, console.getText().getBytes());

	} catch (Exception e) {
	    JRelay.error(e.getMessage());
	}

    }

    public static Label createLabel(String text, int size) {
	Label l = new Label(text);
	l.setStyle("-fx-font-family: \"arial\";" + "-fx-font-size: " + size + "px;");

	return l;
    }

    public ScrollPane buildServerBox() {
	ScrollPane sp1 = new ScrollPane();

	VBox root = new VBox();
	for (Server s : GameData.servers.values()) {
	    Button sButton = createButton(s.name, 12);
	    hookServerChange(sButton);
	    root.getChildren().add(sButton);
	}
	sp1.setMaxHeight(JRelayGUI.APP_HEIGHT - 140);
	sp1.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	sp1.setContent(root);
	return sp1;
    }

    public ScrollPane buildSettingsBox() {

	File file = new File(JRelay.RES_LOC + "settings.properties");
	Properties p = new Properties();
	try {
	    InputStream in = new FileInputStream(file);
	    p.load(in);
	    in.close();

	    GridPane pane = new GridPane();

	    JRelayGUI.settings = JRelay.instance.getSettings();
	    VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(JRelayGUI.settings);

	    VBox vbox1 = new VBox();
	    vbox.setStyle("fx-text-align:right;");
	    vbox1.setSpacing(5);
	    vbox1.setPadding(new Insets(10, 0, 0, 10));
	    Label l = createLabel("Listen Host", 16);
	    Separator s = new Separator();
	    s.setOrientation(Orientation.HORIZONTAL);
	    Label l1 = createLabel("Listen Port", 16);
	    Separator s1 = new Separator();
	    s1.setOrientation(Orientation.HORIZONTAL);
	    Label l2 = createLabel("Use External Proxy", 15);
	    Separator s2 = new Separator();
	    s2.setOrientation(Orientation.HORIZONTAL);
	    Label l3 = createLabel("External Proxy Host", 15);
	    Separator s3 = new Separator();
	    s3.setOrientation(Orientation.HORIZONTAL);
	    Label l4 = createLabel("External Proxy Port", 15);
	    Separator s4 = new Separator();
	    s4.setOrientation(Orientation.HORIZONTAL);
	    Label l5 = createLabel("Remote Host", 15);
	    Separator s5 = new Separator();
	    s5.setOrientation(Orientation.HORIZONTAL);
	    Label l6 = createLabel("Remote Port", 15);
	    Separator s6 = new Separator();
	    s6.setOrientation(Orientation.HORIZONTAL);
	    Label l7 = createLabel("Key 0", 15);
	    Separator s7 = new Separator();
	    s7.setOrientation(Orientation.HORIZONTAL);
	    Label l8 = createLabel("Key 1", 15);
	    Separator s8 = new Separator();
	    s8.setOrientation(Orientation.HORIZONTAL);

	    vbox1.getChildren().addAll(l, s, l1, s1, l2, s2, l3, s3, l4, s4, l5, s5, l6, s6, l7, s7, l8, s8);

	    VBox vbox2 = new VBox();
	    vbox2.setSpacing(5);
	    vbox2.setPadding(new Insets(10, 0, 0, 10));

	    Button save = new Button("Save");
	    hookSaveSettings(save);
	    Button refresh = new Button("Refresh");

	    vbox2.getChildren().addAll(save, refresh);
	    GridPane.setColumnIndex(vbox1, 0);
	    GridPane.setColumnIndex(vbox, 1);
	    GridPane.setColumnIndex(vbox2, 2);
	    pane.getChildren().addAll(vbox1, vbox, vbox2);
	    ScrollPane sp2 = new ScrollPane();
	    sp2.setPadding(new Insets(10, 0, 0, 0));
	    sp2.setMaxHeight(JRelayGUI.APP_HEIGHT - 150);
	    sp2.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	    sp2.setContent(pane);
	    return sp2;
	} catch (Exception e) {
	    JRelay.error(e.getMessage());
	}

	return null;

    }
    public void hookServerChange(Button b) {
    	b.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent e) {
    	    	 Platform.runLater(new Runnable() {
    	 			public void run() {
    	 			   Server toConnect = GameData.nameToServer.get(b.getText());
    	 				if(toConnect==null) {
    	 					 System.out.println("server not found");
    	 					 return;
    	 				}else {
    	 					System.out.println(toConnect.name);
    	 					ReconnectHandler recon = JRelay.instance.reconHandler;
    	 					if(recon==null) {
    	 						 System.out.println("Handler was null!");
    	 						 return;
    	 					}
    	 					recon.onConnectCommand("/jcon "+toConnect.abbreviation, new String[] {"/jcon",toConnect.abbreviation});
    	 				}
    	 			  
    	 				
    	 			}
    	 		    });	
    		
    	    }
    	});
        }
    public void hookRefreshSettings(Button b) {
	b.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		JRelayGUI.settings = JRelay.instance.getSettings();
	    }
	});
    }

    public void hookSaveSettings(Button b) {
	b.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		JRelay.instance.saveSettings(settings);
	    }
	});
    }

    public void hookProxyControl(Button b) {

	b.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		if (!relayStarted) {
		    Platform.runLater(new Runnable() {
			public void run() {
			    log("Starting JRelay...");
			    try {
				new Thread(new JRelay()).start();
				b.setText("Stop Proxy");
				savedLog = false;
				relayStarted = true;
			    } catch (Exception e) {
				error("Unable to start proxy");
				b.setText("Start Proxy");
				JRelayGUI.updateStatus("Not Running", "grey");
				relayStarted = false;
			    }
			}
		    });
		} else {
		    JRelay.instance.shutdown();
		    b.setText("Start Proxy");
		    relayStarted = false;
		    JRelayGUI.updateStatus("Not Running", "grey");
		}
	    }
	});
    }

    public void hookClearConsole(Button b) {
	b.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		console.clear();
	    }
	});
    }

    public TabPane buildTabs() {
	TabPane tabPane = new TabPane();
	tabPane.setStyle("-fx-background-color:white;");
	tabPane.setPadding(new Insets(10, 10, -10, 10));
	Tab informationTab = new Tab();
	
	informationTab.setGraphic(createLabel("Information", 16));
	HBox information = new HBox();
	information.getChildren().add(new Label("Information Tab"));
	information.setAlignment(Pos.TOP_LEFT);
	informationTab.setContent(information);
	tabPane.getTabs().add(informationTab);

	Tab pluginsTab = new Tab();
	pluginsTab.setGraphic(createLabel("Plugins", 16));
	pluginsTab.setContent(buildPluginsTable());
	tabPane.getTabs().add(pluginsTab);



	Tab settingsTab = new Tab();
	settingsTab.setGraphic(createLabel("Settings", 16));
	settingsTab.setContent(buildSettingsBox());
	tabPane.getTabs().add(settingsTab);

	Tab packetsTab = new Tab();
	packetsTab.setStyle("-fx-padding: 5px; -fx-border-insets: 5px; -fx-background-insets: 5px;");
	packetsTab.setGraphic(createLabel("Packets", 16));
	packetsTab.setContent(buildPacketsBox());
	tabPane.getTabs().add(packetsTab);

	Tab aboutTab = new Tab();
	aboutTab.setGraphic(createLabel("About", 16));
	aboutTab.setContent(createAboutTab());
	tabPane.getTabs().add(aboutTab);
	for (Tab t : tabPane.getTabs()) {
	    t.setStyle("-fx-background-color:grey; -fx-font-size:16;");
	    t.getGraphic().setStyle("-fx-text-fill:white;");
	}
	return tabPane;

    }

    public HBox buildPacketsBox() {
	HBox root = new HBox();

	root.setPadding(new Insets(10, 0, 0, 0));
	VBox serverPackets = new VBox();
	serverPackets.getChildren().add(createLabel("Server", 18));
	serverPackets.getChildren().add(new Separator());

	List<Class<? extends Packet>> sPacks = Packet.addServerPackets();

	for (Class<? extends Packet> pack : sPacks) {
	    Button b = createButton(pack.getSimpleName(), 12);
	    PacketMeta sMeta = Packet.getServerPacketMeta(pack.getSimpleName());
	    b.setTooltip(new Tooltip(Packet.buildMetaString(sMeta)));
	    serverPackets.getChildren().add(b);
	    serverPackets.getChildren().add(new Separator());
	}
	ScrollPane sp1 = new ScrollPane();

	sp1.setMaxHeight(JRelayGUI.APP_HEIGHT - 140);
	sp1.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	sp1.setContent(serverPackets);
	VBox clientPackets = new VBox();

	clientPackets.getChildren().add(createLabel("Client", 18));
	clientPackets.getChildren().add(new Separator());
	List<Class<? extends Packet>> cPacks = Packet.addClientPackets();

	for (Class<? extends Packet> pack : cPacks) {
	    Button b = createButton(pack.getSimpleName(), 12);
	    PacketMeta cMeta = Packet.getClientPacketMeta(pack.getSimpleName());
	    b.setTooltip(new Tooltip(Packet.buildMetaString(cMeta)));
	    clientPackets.getChildren().add(b);
	    clientPackets.getChildren().add(new Separator());
	}
	ScrollPane sp2 = new ScrollPane();
	sp2.setMaxHeight(JRelayGUI.APP_HEIGHT - 140);
	sp2.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	sp2.setContent(clientPackets);

	root.getChildren().addAll(sp1, new Separator(), sp2);
	return root;

    }

    public void hookPluginRefreshButton(Button b) {
	b.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		JRelay.instance.reloadPlugins();
		log("Reloaded plugins");
	    }
	});
    }

    public TableView<String> buildPluginsTable() {

	TableView<String> table = new TableView<String>();
	table.setPlaceholder(new Label("No plugins attached"));
	final Label label = new Label("Plugins");
	label.setFont(new Font("Arial", 16));
	// ScrollPane sp2 = new ScrollPane();
	//
	// sp2.setMaxHeight(JRelayGUI.APP_HEIGHT-150);
	// sp2.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	// sp2.setContent(table);

	plugins = table;
	plugins.setPrefSize(300, 300);
	return table;
    }

    public void startPluginUpdate() {
	Runnable task = new Runnable() {
	    public void run() {
		while (true) {
		    updatePlugins();
		    try {
			Thread.sleep(400);
		    } catch (InterruptedException e) {
			JRelay.error(e.getMessage());
		    }
		}
	    }
	};
	Thread backgroundThread = new Thread(task);
	backgroundThread.setDaemon(true);
	backgroundThread.start();

    }

    public void updatePlugins() {

	Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		for (int i = 0; i < plugins.getItems().size(); i++) {
		    plugins.getItems().clear();
		}
	    }
	});

	ArrayList<PluginMetaData> pluginData = JRelay.instance.pluginData;
	if (pluginData.size() > 0) {
	    for (PluginMetaData entry : pluginData) {
		Platform.runLater(new Runnable() {

		    @Override
		    public void run() {
			String res = "Author - " + entry.getAuthor() + "\n";
			res += "Name - " + entry.getName() + "\n";
			res += "Description - " + entry.getDescription() + "\n";

			StringBuilder strBuilder = new StringBuilder();
			for (int i = 0; i < entry.getCommands().length; i++) {
			    strBuilder.append(entry.getCommands()[i] + "\n");
			}
			String commands = strBuilder.toString();

			strBuilder = new StringBuilder();
			for (int i = 0; i < entry.getPackets().length; i++) {
			    strBuilder.append(entry.getPackets()[i] + "\n");
			}
			String packets = strBuilder.toString();
			res += "Commands:\n" + commands;
			res += "Packets:\n" + packets;
			String[] values = new String[] { entry.name, res };
			String[] headers = new String[] { "Name", "Data" };
			for (int i = plugins.getColumns().size(); i < headers.length; i++) {
			    TableColumn<List<String>, String> col = new TableColumn<>(headers[i]);
			    final int colIndex = i;
			    col.setCellValueFactory(data -> {
				List<String> rowValues = data.getValue();
				String cellValue;
				if (colIndex < rowValues.size()) {
				    cellValue = rowValues.get(colIndex);
				} else {
				    cellValue = "";
				}
				return new ReadOnlyStringWrapper(cellValue);
			    });

			    plugins.getColumns().add(col);
			}
			plugins.getItems().add(Arrays.asList(values));
		    }
		});
	    }
	}
    }

    public VBox createAboutTab() {
	VBox box = new VBox();
	box.setAlignment(Pos.TOP_CENTER);
	box.setSpacing(10);
	box.setPadding(new Insets(10, 0, 0, 10));
	Label l = createLabel("JRelay v" + JRelay.JRELAY_VERSION, 24);
	Label l1 = createLabel("For RotMG " + JRelay.GAME_VERSION, 12);
	Label l2 = createLabel("Author - Ruusey", 12);
	Label l3 = createLabel("Credits", 24);
	Label l4 = createLabel("AMPBEdu", 12);
	Label l5 = createLabel("Krazyshank", 12);
	box.getChildren().addAll(l, l1, l2, l3, l4, l5);
	return box;
    }

    public static void createPopup(String message, String action) {
	final Popup popup = new Popup();

	VBox vbox2 = new VBox();
	vbox2.setStyle(
		"-fx-background-radius:5,5,5,5;-fx-background-color:#B8B8B8;-fx-background-radius: 5,5,5,5;-fx-padding: 10;-fx-border-color: grey;");
	vbox2.setAlignment(Pos.CENTER);
	vbox2.setSpacing(5);
	vbox2.setPadding(new Insets(10, 0, 0, 10));
	Button close = new Button(action);
	close.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		popup.hide();

	    }
	});
	vbox2.getChildren().addAll(createLabel(message, 16), close);
	popup.getContent().add(vbox2);
	popup.show(app);
    }

    public static void createExceptionMessage(String message, String action) {
	final Popup popup = new Popup();
	popup.centerOnScreen();

	VBox vbox2 = new VBox();
	vbox2.setStyle(
		"-fx-background-radius:5,5,5,5;-fx-background-color:#FA8072;-fx-background-radius: 5,5,5,5;-fx-padding: 10;-fx-border-color: grey;");
	vbox2.setAlignment(Pos.CENTER);
	vbox2.setSpacing(5);
	vbox2.setPadding(new Insets(10, 0, 0, 10));
	Button close = new Button(action);
	close.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		popup.hide();

	    }
	});
	vbox2.getChildren().addAll(createLabel(message, 8), close);
	popup.getContent().add(vbox2);
	popup.show(app);
    }

    public static void updateStatus(String status, String color) {
	Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		proxyStatus.setText(status);
		proxyStatus.setStyle("-fx-text-fill:" + color + "; -fx-font-size:16;");
	    }
	});

    }

}
