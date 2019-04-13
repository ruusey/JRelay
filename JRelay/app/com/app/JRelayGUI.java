package com.app;

import java.awt.AWTException;
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
import com.move.models.VirtualKeyBoard;
import com.relay.JRelay;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import plugins.ReconnectHandler;

public class JRelayGUI extends Application {
	public static Stage app;
	public static final int APP_HEIGHT = 480;
	public static final int APP_WIDTH = 620;
	public static TextArea console;
	public static TableView<TabPane> pluginMeta;
	public static Label proxyStatus;
	public static boolean relayStarted = false;
	public static ArrayList<TextField> settings;
	public static VirtualKeyBoard kb;
	public static TableView connections;
	public static TableView plugins;

	public static void createExceptionMessage(final String message, final String action) {
		final Popup popup = new Popup();
		popup.centerOnScreen();

		final VBox vbox2 = new VBox();
		vbox2.setStyle(
				"-fx-background-radius:5,5,5,5;-fx-background-color:#FA8072;-fx-background-radius: 5,5,5,5;-fx-padding: 10;-fx-border-color: grey;");
		vbox2.setAlignment(Pos.CENTER);
		vbox2.setSpacing(5);
		vbox2.setPadding(new Insets(10, 0, 0, 10));
		final Button close = new Button(action);
		close.setOnAction(e -> popup.hide());
		vbox2.getChildren().addAll(JRelayGUI.createLabel(message, 8), close);
		popup.getContent().add(vbox2);
		popup.show(JRelayGUI.app);
	}

	public static Label createLabel(final String text, final int size) {
		final Label l = new Label(text);
		l.setStyle("-fx-font-family: \"arial\";" + "-fx-font-size: " + size + "px;");
		return l;
	}

	public static void createPopup(final String message, final String action) {
		final Popup popup = new Popup();

		final VBox vbox2 = new VBox();
		vbox2.setStyle(
				"-fx-background-radius:5,5,5,5;-fx-background-color:#B8B8B8;-fx-background-radius: 5,5,5,5;-fx-padding: 10;-fx-border-color: grey;");
		vbox2.setAlignment(Pos.CENTER);
		vbox2.setSpacing(5);
		vbox2.setPadding(new Insets(10, 0, 0, 10));
		final Button close = new Button(action);
		close.setOnAction(e -> popup.hide());
		vbox2.getChildren().addAll(JRelayGUI.createLabel(message, 16), close);
		popup.getContent().add(vbox2);
		popup.show(JRelayGUI.app);
	}

	public static void error(final String s) {
		try {
			JRelayGUI.printLog("[SEVERE] " + s + "\n");
		} catch (final Exception e) {
			JRelayGUI.error(e.getMessage());
		}
	}

	public static void log(final String s) {
		try {
			JRelayGUI.printLog("[INFO] " + s + "\n");
		} catch (final Exception e) {
			JRelayGUI.error(e.getMessage());
		}
	}

	public static void main(final String[] args) {
		Application.launch(args);
	}

	public static void printLog(final String s) throws IOException {
		Platform.runLater(() -> JRelayGUI.console.appendText(s));
	}

	public static void updateStatus(final String status, final String color) {
		Platform.runLater(() -> {
			JRelayGUI.proxyStatus.setText(status);
			JRelayGUI.proxyStatus.setStyle("-fx-text-fill:" + color + "; -fx-font-size:16;");
		});

	}

	public static void warn(final String s) {
		try {
			JRelayGUI.printLog("[WARN] " + s + "\n");
		} catch (final Exception e) {
			JRelay.error(e.getMessage());
		}
	}
	public boolean savedLog = false;

	public VBox buildConsole() {
		final VBox consolePane = new VBox();
		consolePane.setPadding(new Insets(5, 5, 5, 5));
		final HBox consoleOptions = new HBox();

		final Separator separator1 = new Separator();
		separator1.setOrientation(Orientation.HORIZONTAL);
		final Separator separator2 = new Separator();
		separator2.setOrientation(Orientation.HORIZONTAL);

		final Button start = this.createButton("Start Proxy", 14);
		this.hookProxyControl(start);
		final Button saveCon = this.createButton("Save Log", 14);
		this.hookSaveConsole(saveCon);
		final Button clearCon = this.createButton("Clear Console", 14);
		this.hookClearConsole(clearCon);

		consoleOptions.getChildren().addAll(start, separator1, saveCon, separator2, clearCon);
		consoleOptions.setPadding(new Insets(0, 0, 5, 0));
		// CONSOLE TEXT AREA
		final TextArea con = new TextArea();
		con.setEditable(false);
		con.setPrefWidth((JRelayGUI.APP_WIDTH * 2) / 3);
		con.setPrefHeight((JRelayGUI.APP_HEIGHT * 2) / 3);
		con.setWrapText(false);

		final Label status = JRelayGUI.createLabel("Not Running", 16);
		JRelayGUI.proxyStatus = status;
		JRelayGUI.console = con;
		consolePane.getChildren().addAll(consoleOptions, con, status);
		return consolePane;
	}

	public HBox buildPacketsBox() {
		final HBox root = new HBox();

		root.setPadding(new Insets(10, 0, 0, 0));
		final VBox serverPackets = new VBox();
		serverPackets.getChildren().add(JRelayGUI.createLabel("Server", 18));
		serverPackets.getChildren().add(new Separator());

		final List<Class<? extends Packet>> sPacks = Packet.addServerPackets();

		for (final Class<? extends Packet> pack : sPacks) {
			final Button b = this.createButton(pack.getSimpleName(), 14);
			final PacketMeta sMeta = Packet.getServerPacketMeta(pack.getSimpleName());
			b.setTooltip(new Tooltip(Packet.buildMetaString(sMeta)));
			serverPackets.getChildren().add(b);
			serverPackets.getChildren().add(new Separator());
		}
		final ScrollPane sp1 = new ScrollPane();

		sp1.setMaxHeight(JRelayGUI.APP_HEIGHT - 140);
		sp1.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp1.setContent(serverPackets);
		final VBox clientPackets = new VBox();

		clientPackets.getChildren().add(JRelayGUI.createLabel("Client", 18));
		clientPackets.getChildren().add(new Separator());
		final List<Class<? extends Packet>> cPacks = Packet.addClientPackets();

		for (final Class<? extends Packet> pack : cPacks) {
			final Button b = this.createButton(pack.getSimpleName(), 14);
			final PacketMeta cMeta = Packet.getClientPacketMeta(pack.getSimpleName());
			b.setTooltip(new Tooltip(Packet.buildMetaString(cMeta)));
			clientPackets.getChildren().add(b);
			clientPackets.getChildren().add(new Separator());
		}
		final ScrollPane sp2 = new ScrollPane();
		sp2.setMaxHeight(JRelayGUI.APP_HEIGHT - 140);
		sp2.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp2.setContent(clientPackets);

		root.getChildren().addAll(sp1, new Separator(), sp2);
		return root;
	}

	public TableView<String> buildPluginsTable() {
		this.plugins = null;
		final TableView<String> table = new TableView<>();
		table.setPlaceholder(new Label("No plugins attached"));
		final Label label = new Label("Plugins");
		label.setFont(new Font("Arial", 16));
		final ScrollPane sp2 = new ScrollPane();

		sp2.setMaxHeight(JRelayGUI.APP_HEIGHT - 150);
		sp2.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp2.setContent(table);

		this.plugins = table;
		this.plugins.setPrefSize(400, 350);
		return table;
	}

	public ScrollPane buildServerBox() {
		final ScrollPane sp1 = new ScrollPane();
		final VBox root = new VBox();
		for (final Server s : GameData.servers.values()) {
			final Button sButton = this.createButton(s.name, 16);
			this.hookServerChange(sButton);
			root.getChildren().add(sButton);
		}
		sp1.setMaxHeight(JRelayGUI.APP_HEIGHT - 140);
		sp1.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp1.setContent(root);
		return sp1;
	}

	public ScrollPane buildSettingsBox() {

		final File file = new File(JRelay.RES_LOC + "settings.properties");
		final Properties p = new Properties();
		try {
			final InputStream in = new FileInputStream(file);
			p.load(in);
			in.close();

			final GridPane pane = new GridPane();

			JRelayGUI.settings = JRelay.instance.getSettings();
			final VBox vbox = new VBox();
			vbox.setSpacing(5);
			vbox.setPadding(new Insets(10, 10, 10, 10));
			vbox.getChildren().addAll(JRelayGUI.settings);

			final VBox vbox1 = new VBox();
			vbox.setStyle("fx-text-align:right;");
			vbox1.setSpacing(5);
			vbox1.setPadding(new Insets(10, 10, 10, 10));
			final Label l = JRelayGUI.createLabel("Listen Host", 16);
			final Separator s = new Separator();
			s.setOrientation(Orientation.HORIZONTAL);
			final Label l1 = JRelayGUI.createLabel("Listen Port", 16);
			final Separator s1 = new Separator();
			s1.setOrientation(Orientation.HORIZONTAL);
			final Label l2 = JRelayGUI.createLabel("Use External Proxy", 15);
			final Separator s2 = new Separator();
			s2.setOrientation(Orientation.HORIZONTAL);
			final Label l3 = JRelayGUI.createLabel("External Proxy Host", 15);
			final Separator s3 = new Separator();
			s3.setOrientation(Orientation.HORIZONTAL);
			final Label l4 = JRelayGUI.createLabel("External Proxy Port", 15);
			final Separator s4 = new Separator();
			s4.setOrientation(Orientation.HORIZONTAL);
			final Label l5 = JRelayGUI.createLabel("Remote Host", 15);
			final Separator s5 = new Separator();
			s5.setOrientation(Orientation.HORIZONTAL);
			final Label l6 = JRelayGUI.createLabel("Remote Port", 15);
			final Separator s6 = new Separator();
			s6.setOrientation(Orientation.HORIZONTAL);
			final Label l7 = JRelayGUI.createLabel("Key 0", 15);
			final Separator s7 = new Separator();
			s7.setOrientation(Orientation.HORIZONTAL);
			final Label l8 = JRelayGUI.createLabel("Key 1", 15);
			final Separator s8 = new Separator();
			s8.setOrientation(Orientation.HORIZONTAL);
			final Label l9 = JRelayGUI.createLabel("Autonexus", 15);
			final Separator s9 = new Separator();
			s9.setOrientation(Orientation.HORIZONTAL);
			final Label l10 = JRelayGUI.createLabel("Star Filter", 15);
			final Separator s10 = new Separator();
			s10.setOrientation(Orientation.HORIZONTAL);
			final Label l11 = JRelayGUI.createLabel("Home Server", 15);
			final Separator s11 = new Separator();
			s11.setOrientation(Orientation.HORIZONTAL);

			vbox1.getChildren().addAll(l, s, l1, s1, l2, s2, l3, s3, l4, s4, l5, s5, l6, s6, l7, s7, l8, s8, l9, s9,
					l10, s10, l11, s11);

			final VBox vbox2 = new VBox();
			vbox2.setSpacing(5);
			vbox2.setPadding(new Insets(10, 10, 10, 10));

			final Button save = new Button("Save");
			this.hookSaveSettings(save);
			final Button refresh = new Button("Refresh");

			vbox2.getChildren().addAll(save, refresh);
			GridPane.setColumnIndex(vbox1, 0);
			GridPane.setColumnIndex(vbox, 1);
			GridPane.setColumnIndex(vbox2, 2);
			pane.getChildren().addAll(vbox1, vbox, vbox2);

			final ScrollPane sp2 = new ScrollPane();
			sp2.setPadding(new Insets(10, 10, 10, 10));
			sp2.setMaxHeight(JRelayGUI.APP_HEIGHT - 150);
			sp2.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			sp2.setContent(pane);
			return sp2;
		} catch (final Exception e) {
			JRelayGUI.error(e.getMessage());
		}
		return null;
	}

	public TabPane buildTabs() {
		final TabPane tabPane = new TabPane();
		tabPane.setStyle("-fx-background-color:white;");
		tabPane.setPadding(new Insets(10, 10, 10, 10));
		final Tab informationTab = new Tab();

		informationTab.setGraphic(JRelayGUI.createLabel("Information", 16));
		final HBox information = new HBox();
		information.getChildren().add(new Label("Information Tab"));
		information.setAlignment(Pos.TOP_LEFT);
		informationTab.setContent(information);
		tabPane.getTabs().add(informationTab);

		final Tab pluginsTab = new Tab();
		pluginsTab.setGraphic(JRelayGUI.createLabel("Plugins", 16));
		pluginsTab.setContent(this.buildPluginsTable());
		tabPane.getTabs().add(pluginsTab);

		final Tab settingsTab = new Tab();
		settingsTab.setStyle("-fx-padding: 5px; -fx-border-insets: 5px; -fx-background-insets: 5px;");
		settingsTab.setGraphic(JRelayGUI.createLabel("Settings", 16));

		settingsTab.setContent(this.buildSettingsBox());
		tabPane.getTabs().add(settingsTab);

		final Tab packetsTab = new Tab();
		packetsTab.setStyle("-fx-padding: 5px; -fx-border-insets: 5px; -fx-background-insets: 5px;");
		packetsTab.setGraphic(JRelayGUI.createLabel("Packets", 16));
		packetsTab.setContent(this.buildPacketsBox());
		tabPane.getTabs().add(packetsTab);

		final Tab aboutTab = new Tab();
		aboutTab.setGraphic(JRelayGUI.createLabel("About", 16));
		aboutTab.setContent(this.createAboutTab());
		tabPane.getTabs().add(aboutTab);
		for (final Tab t : tabPane.getTabs()) {
			t.setStyle("-fx-background-color:white; -fx-font-size:16;");
			t.getGraphic().setStyle("-fx-text-fill:grey;");
		}
		return tabPane;
	}

	public VBox createAboutTab() {
		final VBox box = new VBox();
		box.setAlignment(Pos.TOP_CENTER);
		box.setSpacing(10);
		box.setPadding(new Insets(10, 0, 0, 10));
		final Label l = JRelayGUI.createLabel("JRelay v" + JRelay.JRELAY_VERSION, 24);
		final Label l1 = JRelayGUI.createLabel("For RotMG " + JRelay.GAME_VERSION, 12);
		final Label l2 = JRelayGUI.createLabel("Author - Ruusey", 12);
		final Label l3 = JRelayGUI.createLabel("Credits", 24);
		final Label l4 = JRelayGUI.createLabel("AMPBEdu", 12);
		final Label l5 = JRelayGUI.createLabel("Krazyshank", 12);
		box.getChildren().addAll(l, l1, l2, l3, l4, l5);
		return box;
	}

	private Button createButton(final String string, final int size) {
		final Button text = new Button(string);
		text.setStyle("-fx-font-family: \"arial\";" + "-fx-font-size: " + size + "px;");
		return text;
	}

	public void hookClearConsole(final Button b) {
		b.setOnAction(e -> JRelayGUI.console.clear());
	}

	public void hookProxyControl(final Button b) {
		b.setOnAction(e -> {
			if (!JRelayGUI.relayStarted) {
				Platform.runLater(() -> {
					JRelayGUI.log("Starting JRelay...");
					try {
						new Thread(new JRelay()).start();
						b.setText("Stop Proxy");
						JRelayGUI.this.savedLog = false;
						JRelayGUI.relayStarted = true;
					} catch (final Exception e1) {
						JRelayGUI.error("Unable to start proxy");
						b.setText("Start Proxy");
						JRelayGUI.updateStatus("Not Running", "grey");
						JRelayGUI.relayStarted = false;
					}
				});
			} else {
				JRelay.instance.shutdown();
				b.setText("Start Proxy");
				JRelayGUI.relayStarted = false;
				JRelayGUI.updateStatus("Not Running", "grey");
			}
		});
	}

	public void hookRefreshSettings(final Button b) {
		b.setOnAction(e -> JRelayGUI.settings = JRelay.instance.getSettings());
	}

	public void hookSaveConsole(final Button b) {
		b.setOnAction(e -> {
			JRelayGUI.this.saveConsole();
			JRelayGUI.createPopup("Console log saved under 'logs/'", "Ok");

		});
	}

	public void hookSaveSettings(final Button b) {
		b.setOnAction(e -> JRelay.instance.saveSettings(JRelayGUI.settings));
	}

	public void hookServerChange(final Button b) {
		b.setOnAction(e -> Platform.runLater(() -> {
			final Server toConnect = GameData.nameToServer.get(b.getText());
			if (toConnect == null) {
				JRelayGUI.error("Server not found!");
				return;
			} else {
				JRelayGUI.log(toConnect.name);
				final ReconnectHandler recon = JRelay.instance.reconHandler;
				if (recon == null) {
					JRelayGUI.error("Handler was null!");
					return;
				}
				recon.onConnectCommand("/jcon " + toConnect.abbreviation,
						new String[] { "/jcon", toConnect.abbreviation });
			}
		}));
	}

	public void saveConsole() {
		final String pattern = "yyyy-MM-dd HH-mm-ss";
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		final String date = simpleDateFormat.format(new Date());
		try {
			final Path file = Paths.get(JRelay.RES_LOC + "logs//console_log" + date + ".txt");
			Files.write(file, JRelayGUI.console.getText().getBytes());
		} catch (final Exception e) {
			JRelayGUI.error(e.getMessage());
		}
	}

	@Override
	public void start(final Stage primaryStage) {
		
		JRelayGUI.app = primaryStage;
		primaryStage.getIcons().add(new Image("icon.png"));
		primaryStage.setTitle("JRelay - RotMG Proxy");
		primaryStage.setResizable(false);

		final Group root = new Group();
		 Scene scene = new Scene(root, JRelayGUI.APP_WIDTH, JRelayGUI.APP_HEIGHT, Color.WHITE);
		scene.getStylesheets().addAll("app.css");
		
		
		final ImageView imv = new ImageView();
		final Image logo = new Image("icon.png");
		imv.setImage(logo);
		imv.setFitWidth(70);
		imv.setPreserveRatio(true);
		imv.setSmooth(true);
		imv.setCache(true);

		final HBox hb = new HBox();

		final Label header = JRelayGUI.createLabel("JRelay", 26);
		header.setPadding(new Insets(16, 0, 0, 5));
		hb.setPadding(new Insets(14, 0, 0, 10));
		hb.getChildren().addAll(imv, header);

		final BorderPane componentLayout = new BorderPane();
		if (!GameData.loadData()) {
			JRelayGUI.error("GameData unable to load. Exiting...");
			System.exit(0);
		}
		final TabPane tabs = this.buildTabs();
		tabs.getTabs().get(0).setContent(this.buildConsole());

		componentLayout.setTop(hb);
		componentLayout.setCenter(tabs);

		root.getChildren().add(componentLayout);
		
		// Add the Scene to the Stage
		primaryStage.setScene(scene);
		primaryStage.show();
	
		primaryStage.setOnCloseRequest(event -> {
			JRelay.instance.shutdown();
			Platform.exit();
			
			//kb.releaseKeys("w+a+s+d");
			System.exit(0);
			
		});
		//ResourceMonitor monitor =new ResourceMonitor();
		//final Thread td = new Thread(monitor);
		//td.start();
		JRelayGUI.log("JRelay for RotMG " + JRelay.GAME_VERSION);
		
		
		
	}

	public static void startPluginUpdate() {
		final Runnable task = new Runnable() {
			@Override
			public void run() {
				while (!JRelayGUI.updatePlugins()) {
					try {
						this.wait(1000);
					} catch (final InterruptedException e) {
						JRelayGUI.error(e.getMessage());
					}
				}
				JRelayGUI.updatePlugins();
				try {
					this.finalize();
				} catch (final Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		final Thread backgroundThread = new Thread(task);
		backgroundThread.run();
	

	}

	public static boolean updatePlugins() {
		
		if (JRelayGUI.plugins.getItems().size() > 1) {
			return false;
		}

		final ArrayList<PluginMetaData> pluginData = JRelay.instance.pluginData;
		if (pluginData.size() > 0) {

			Platform.runLater(() -> {
				JRelayGUI.plugins.getItems().clear();
				for (final PluginMetaData entry : pluginData) {
					String res = "Author - " + entry.getAuthor() + "\n";
					res += "Name - " + entry.getName() + "\n";
					res += "Description - " + entry.getDescription() + "\n";

					StringBuilder strBuilder = new StringBuilder();
					for (int i1 = 0; i1 < entry.getCommands().length; i1++) {
						strBuilder.append(entry.getCommands()[i1] + "\n");
					}
					final String commands = strBuilder.toString();

					strBuilder = new StringBuilder();
					for (int i2 = 0; i2 < entry.getPackets().length; i2++) {
						strBuilder.append(entry.getPackets()[i2] + "\n");
					}
					final String packets = strBuilder.toString();
					res += "Commands:\n" + commands;
					res += "Packets:\n" + packets;
					final String[] values = new String[] { entry.name, res };
					final String[] headers = new String[] { "Name", "Data" };
					for (int i3 = JRelayGUI.plugins.getColumns().size(); i3 < headers.length; i3++) {
						final TableColumn<List<String>, String> col = new TableColumn<>(headers[i3]);
						final int colIndex = i3;
						col.setCellValueFactory(data -> {
							final List<String> rowValues = data.getValue();
							String cellValue;
							if (colIndex < rowValues.size()) {
								cellValue = rowValues.get(colIndex);
							} else {
								cellValue = "";
							}
							return new ReadOnlyStringWrapper(cellValue);
						});
						JRelayGUI.plugins.getColumns().add(col);
					}
					JRelayGUI.plugins.getItems().add(Arrays.asList(values));
				}
			});
			return true;
		}else {
			return false;
		}
		
		
	}

}
