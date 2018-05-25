package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Main extends Application {
	int n = 15, m = 15;
	AnchorPane pan = new AnchorPane();
	Button btn[][] = new Button[n][m];
	ButtonController ctrl = new ButtonController();
	Button newGamebtn = new Button();
	Button deleteYourLastMove = new Button();
	int point = 0; // 番を扱う変数 ( Variable controlling player's turn )

	// 将棋版を作ります。( Create a board)
	public Parent CreateContent() {
		pan.setPrefSize(705, 600);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				btn[i][j] = new Button();
				btn[i][j].setPrefSize(40, 40);
				btn[i][j].setTranslateX(j * 40);
				btn[i][j].setTranslateY(i * 40);
				pan.getChildren().add(btn[i][j]);
				btn[i][j].setOnMouseClicked(ctrl.getHandler());
			}
		}
		// Draw a line.
		Line line = new Line();
		line.setStartX(601);
		line.setEndX(600);
		line.setStartY(0);
		line.setEndY(600);
		line.setStrokeWidth(5f);
		line.setFill(Color.RED);

		// ImageView backimg = new ImageView("C:\\Users\\Da\\Downloads\\backarrow.png");

		// Create a "New Game" Button.
		newGamebtn = new Button("New Game");
		newGamebtn.setPrefSize(100, 100);
		newGamebtn.setLayoutX(605);
		newGamebtn.setLayoutY(101);
		
		// "DeleteYourLastMove" Button
		deleteYourLastMove = new Button("Back");
		deleteYourLastMove.setPrefSize(100, 100);
		deleteYourLastMove.setLayoutX(605);
		deleteYourLastMove.setLayoutY(0);
		pan.getChildren().addAll(deleteYourLastMove, newGamebtn, line);
		newGamebtn.setOnMouseClicked(ctrl.getHandler());

		return pan;
	}

	public boolean checkWin(int x, int y, String name) {
		int k, j;
		int d = 0;
		// 縦をチェックします。（Check columns)
		for (k = -4; k <= 4; k++) {
			if (x + k >= 0 && x + k < n) {

				if (btn[x + k][y].getText() == name) {
					d++;

					// To debug
					// System.out.println("X: " + x);
					// System.out.println("K: " + k);
					// System.out.println("X+K: " + (x + k));
					// System.out.println("D: " + d);
					// System.out.println("");
				} else if (d < 5) {
					d = 0;
				}
			}
		}
		if (d >= 5) {
			return true;
		} else {
			d = 0;
		}

		// 横をチェックします。( check rows)
		for (k = -4; k <= 4; k++) {
			if (y + k >= 0 && y + k < n) {
				if (btn[x][y + k].getText() == name) {
					d++;
				} else if (d < 5) {
					d = 0;
				}
			}
			if (d >= 5) {
				return true;
			} else {
				d = 0;
			}
		}

		// 斜めをチェックします。(Check the diagonal)

		// 左へ。( check the diagonal going to the left)
		for (k = -4, j = 4; k <= 4 && j >= -4; k++, j--) {
			if (y + k >= 0 && y + k < n && x + j >= 0 && x + j < m) {
				if (btn[x + j][y + k].getText() == name) {
					d++;
				} else if (d < 5) {
					d = 0;
				}
			}
		}
		if (d >= 5) {
			return true;
		} else {
			d = 0;
		}

		// 右へ。( check the diagonal going to the right)
		for (k = -4; k <= 4; k++) {
			if (y + k >= 0 && y + k < n && x + k >= 0 && x + k < m) {

				if (btn[x + k][y + k].getText() == name) {
					d++;
				} else if (d < 5) {
					d = 0;
				}

			}
		}
		if (d >= 5) {
			return true;
		}

		return false;
	}

	// "O"と"X"を入れます.(Class setting 'O' or 'X' )
	private class ButtonController {

		private EventHandler<MouseEvent> EH;

		private ButtonController() {
			EH = new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent e) {
					// TODO Auto-generated method stub

					for (int i = 0; i < n; i++) {
						for (int j = 0; j < m; j++) {

							if (e.getSource() == newGamebtn) {
								btn[i][j].setText("");
								// undo(i,j);

							}
							// "O""X"を入れる条件 (Conditions of inputting "X" and "O")
							if (e.getSource() == btn[i][j] && btn[i][j].getText() != "X"
									&& btn[i][j].getText() != "O") {
								if (point % 2 == 0) {
									btn[i][j].setText("X");
									btn[i][j]
											.setStyle("-fx-text-fill: #FF3F33;-fx-font-weight: bold;-fx-font-size: 17");
									point++;

									// 勝ったら、処理します。(To take action when "X" or "O" wins)
									if (checkWin(i, j, ((btn[i][j].getText())))) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Information Dialog");
										alert.setHeaderText(null);
										alert.setContentText(btn[i][j].getText() + " Win!");
										alert.showAndWait();

										// 将棋版に"X"と"O"を消します。(To create a new board)
										for (int i1 = 0; i1 < n; i1++) {
											for (int j1 = 0; j1 < m; j1++) {
												btn[i1][j1].setText("");
												point = 0;

											}
										}
									}

								} else {
									btn[i][j].setText("O");
									btn[i][j]
											.setStyle("-fx-text-fill: #0000ff;-fx-font-weight: bold;-fx-font-size: 17");
									point++;
									// 勝ったら、処理します。(To take action when "X" or "O" wins)
									if (checkWin(i, j, ((btn[i][j].getText())))) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Information Dialog");
										alert.setHeaderText(null);
										alert.setContentText(btn[i][j].getText() + " Win!");
										alert.showAndWait();

										// 将棋版に"X"と"O"を消します。(To create a new board)
										for (int i1 = 0; i1 < n; i1++) {
											for (int j1 = 0; j1 < m; j1++) {
												btn[i1][j1].setText("");
												point = 0;

											}
										}
									}
								}

							}
						}
					}

				}

			};

		}

		public EventHandler<MouseEvent> getHandler() {
			return EH;
		}

	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setScene(new Scene(CreateContent()));
		primaryStage.setTitle("Gomokunarabe");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {

		launch(args);

	}
}
