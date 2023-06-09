package com.ifsc.tds.caio.gabriel.jose.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.ifsc.tds.caio.gabriel.jose.dao.EmprestimosDAO;
import com.ifsc.tds.caio.gabriel.jose.enity.Emprestimos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EmprestimosListaController implements Initializable {

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private SplitPane pnlDivisao;

	@FXML
	private AnchorPane pnlEsquerda;

	@FXML
	private TableView<Emprestimos> tbvEmprestimos;

	@FXML
	private TableColumn<Emprestimos, Long> tbcCodigo;

	@FXML
	private TableColumn<Emprestimos, Long> tbcNome;

	@FXML
	private AnchorPane pnlDireita;

	@FXML
	private Label lblDetalhes;

	@FXML
	private GridPane pnlDetalhes;

	@FXML
	private Label lblDataEmpre;

	@FXML
	private Label lblDataDev;

	@FXML
	private Label lblDataEmpreValor;

	@FXML
	private Label lblDataDevValor;

	@FXML
	private Label lblCliente;

	@FXML
	private Label lblClienteValor;

	@FXML
	private Label lblFilme;

	@FXML
	private Label lblFilmeValor;

    @FXML
    private Label lblDescricao;

    @FXML
    private Label lblDescricaoValor;

	@FXML
	private ButtonBar barBotoes;

	@FXML
	private Button btnInclur;

	@FXML
	private Tooltip tlpIncluir;

	@FXML
	private Button btnEditar;

	@FXML
	private Tooltip tlpEditar;

	@FXML
	private Button btnExcluir;

	@FXML
	private Tooltip tlpExcluir;

	private List<Emprestimos> listaEmprestimos;
	private ObservableList<Emprestimos> observableListaEmprestimos = FXCollections.observableArrayList();
	private EmprestimosDAO emprestimosDAO;

	public static final String EMPRESTIMOS_EDITAR = "- Editar";
	public static final String EMPRESTIMOS_INCLUIR = "- Incluir";
	
	
	@FXML
	void onClickBtnEditar(ActionEvent event) {
		Emprestimos emprestimos = this.tbvEmprestimos.getSelectionModel().getSelectedItem();

		if (emprestimos != null) {
			boolean btnConfirmarClic = this.onShowTelaEmprestimosEditar(emprestimos,
					EmprestimosListaController.EMPRESTIMOS_EDITAR);

			if (btnConfirmarClic) {
				this.getEmprestimosDAO().update(emprestimos, null);
				this.carregarTableViewEmprestimos();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);

			//alerta.setContentText("Por favor, escolha um empréstimo na tabela!");

			alerta.setContentText("Por favor, escolha um filme na tabela!");

			alerta.show();
		}
	}

	@FXML
	void onClickBtnExcluir(ActionEvent event) {
		Emprestimos emprestimos = this.tbvEmprestimos.getSelectionModel().getSelectedItem();

		if (emprestimos != null) {

			Alert alerta = new Alert(AlertType.CONFIRMATION);
			alerta.setTitle("Pergunta");
			alerta.setHeaderText("Confirma a exclusão de empréstimos?\n" + emprestimos.getId());

			ButtonType botaoNao = ButtonType.NO;
			ButtonType botaoSim = ButtonType.YES;
			alerta.getButtonTypes().setAll(botaoSim, botaoNao);
			Optional<ButtonType> resultado = alerta.showAndWait();

			if (resultado.get() == botaoSim) {
				this.getEmprestimosDAO().delete(emprestimos);
				this.carregarTableViewEmprestimos();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setContentText("Por favor, escolha um empréstimo na tabela!");
			alerta.show();
		}
	}

	@FXML
	void onClickBtnIncluir(ActionEvent event) {
		Emprestimos emprestimos = new Emprestimos();

		boolean btnConfirmarClic = this.onShowTelaEmprestimosEditar(emprestimos,
				EmprestimosListaController.EMPRESTIMOS_INCLUIR);

		if (btnConfirmarClic) {
			this.getEmprestimosDAO().save(emprestimos);
			this.carregarTableViewEmprestimos();
		}
	}

	private void carregarTableViewEmprestimos() {
		this.tbcCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.tbcNome.setCellValueFactory(new PropertyValueFactory<>("descr"));

		this.setListaEmprestimos(this.getEmprestimosDAO().getAll());
		this.setObservableListaEmprestimos(FXCollections.observableArrayList(this.getListaEmprestimos()));
		this.tbvEmprestimos.setItems(this.getObservableListaEmprestimos());
	}

	private boolean onShowTelaEmprestimosEditar(Emprestimos emprestimos, String emprestimosEditar) {
		try {
			// carregando o loader
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/ifsc/tds/caio/gabriel/jose/view/EmprestimosEdit.fxml"));
			Parent emprestimosEditXML = loader.load();

			// criando uma janela nova
			Stage janelaEmprestimosEditar = new Stage();
			janelaEmprestimosEditar.setTitle("Cadastro de emprestimos" + emprestimosEditar);
			janelaEmprestimosEditar.initModality(Modality.APPLICATION_MODAL);
			janelaEmprestimosEditar.resizableProperty().setValue(Boolean.FALSE);

			Scene emprestimosEditLayout = new Scene(emprestimosEditXML);
			janelaEmprestimosEditar.setScene(emprestimosEditLayout);

			// carregando o controller e a scene
			EmprestimosEditController emprestimosEditController = loader.getController();
			emprestimosEditController.setJanelaEmprestimosEdit(janelaEmprestimosEditar);
			emprestimosEditController.populaTela(emprestimos);

			// mostrando a tela
			janelaEmprestimosEditar.showAndWait();

			return emprestimosEditController.isOkClick();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public  boolean onCloseQuery() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Pergunta");
		alerta.setHeaderText("Deseja sair do cadastro de empréstimo?");
		ButtonType buttonTypeNO = ButtonType.NO;
		ButtonType buttonTypeYES = ButtonType.YES;
		alerta.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
		Optional<ButtonType> result = alerta.showAndWait();
		return result.get() == buttonTypeYES ? true : false;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setEmprestimosDAO(new EmprestimosDAO());
		this.carregarTableViewEmprestimos();
		this.selecionarItemTableViewEmprestimos(null);

		this.tbvEmprestimos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewEmprestimos(newValue));
	}

	public void selecionarItemTableViewEmprestimos(Emprestimos emprestimos) {
		if (emprestimos != null) {
			this.lblDataEmpreValor.setText(emprestimos.getDataEmpreFormatada().toString());
			this.lblDataDevValor.setText(emprestimos.getDataDevFormatada().toString());
			this.lblDescricaoValor.setText(emprestimos.getDescr());
			this.lblClienteValor.setText(emprestimos.getClientes().getNome());
			this.lblFilmeValor.setText(emprestimos.getFilmes().getNomeFilme());
		} else {
			this.lblDataEmpreValor.setText("");
			this.lblDataDevValor.setText("");
			this.lblDescricaoValor.setText("");
			this.lblClienteValor.setText("");
			this.lblFilmeValor.setText("");
		}
	}

	public List<Emprestimos> getListaEmprestimos() {
		return listaEmprestimos;
	}

	public void setListaEmprestimos(List<Emprestimos> listaEmprestimos) {
		this.listaEmprestimos = listaEmprestimos;
	}

	public ObservableList<Emprestimos> getObservableListaEmprestimos() {
		return observableListaEmprestimos;
	}

	public void setObservableListaEmprestimos(ObservableList<Emprestimos> observableListaEmprestimos) {
		this.observableListaEmprestimos = observableListaEmprestimos;
	}

	public EmprestimosDAO getEmprestimosDAO() {
		return emprestimosDAO;
	}

	public void setEmprestimosDAO(EmprestimosDAO emprestimosDAO) {
		this.emprestimosDAO = emprestimosDAO;
	}
}
