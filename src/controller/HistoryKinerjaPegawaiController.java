package controller;

import java.sql.SQLException;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connection.DbConnection;
import model.dao.HistoryKinerjaPegawaiDAO;
import model.dao.KelolaPegawaiDAO;
import model.dto.HasilKinerjaPegawaiDTO;
import model.dto.KelolaPegawaiDTO;
import util.CustomeUtil;



public class HistoryKinerjaPegawaiController extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Window winHistoryKinerjaPegawai;
	
	Textbox txtSearchNip;
	Textbox txtNamaPegawai;
	
	Button btnCariNip;
	
	Button btnSearch;
	Button btnClear;
	
	Bandbox bdxNip;
	
	Listbox lbxNip;
	Listbox lbxHasilKinerja;
	
	String nip;
	String nama;
	

	private ListModelList<HasilKinerjaPegawaiDTO> lmlHistoryKinerja;
	
	private List<HasilKinerjaPegawaiDTO> lstDataHistoryKinerja;
	
	private List<KelolaPegawaiDTO> lsNip;

	KelolaPegawaiDAO kelPegDAO = new KelolaPegawaiDAO(DbConnection.getDBConnection());
	ListModelList<KelolaPegawaiDTO> modelKelPeg = new ListModelList<KelolaPegawaiDTO>();
	
	HistoryKinerjaPegawaiDAO hslKinPegDAO = new HistoryKinerjaPegawaiDAO(DbConnection.getDBConnection());
	ListModelList<HasilKinerjaPegawaiDTO> lmlHslKinPegDTO = new ListModelList<HasilKinerjaPegawaiDTO>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initComponents();
	}

	private void initComponents() {
	}


	public void onClick$btnClear(){
		bdxNip.setValue("");
		txtNamaPegawai.setValue("");
		if (lmlHistoryKinerja != null) {
			lmlHistoryKinerja.clear();
			lstDataHistoryKinerja.clear();
		}
	}
	
	public void onClick$btnSearch(){
		if (isValidate()) {
			String strNip= nip.toLowerCase().trim();
			lmlHslKinPegDTO = new ListModelList<HasilKinerjaPegawaiDTO>(hslKinPegDAO.findAll(strNip));
			lstDataHistoryKinerja = lmlHslKinPegDTO;
			if (lstDataHistoryKinerja.isEmpty()) {
				CustomeUtil.infoWithTitle("Data yang anda cari tidak ditemukan", "Informasi");
			}
			renderListBox(lstDataHistoryKinerja);
		}
	}
	
	private void renderListBox(List<HasilKinerjaPegawaiDTO> lstDataHasilKinerja) {
		
		lmlHistoryKinerja = new ListModelList<HasilKinerjaPegawaiDTO>(lstDataHasilKinerja);
		lbxHasilKinerja.setModel(lmlHistoryKinerja);
		lbxHasilKinerja.setItemRenderer(new ListitemRenderer<Object>() {
			
			@Override
			public void render(Listitem li, Object data, int arg) throws Exception {
					
//				Map<String, Object> hasilkinerja = (Map<String, Object>) data;
				
				final HasilKinerjaPegawaiDTO hasilkinerja = (HasilKinerjaPegawaiDTO) data;
				
				String strPendidikan = "";
				if (hasilkinerja.getPendidikan().toString().equals("1")) {
					strPendidikan = "SMA";
				} else if (hasilkinerja.getPendidikan().toString().equals("2")){
					strPendidikan = "D3";
				} else {
					strPendidikan = "S1";
				}

				Label lblPendidikan = new Label(strPendidikan);
				Label lblKehadiran = new Label(hasilkinerja.getKehadiran().toString());
				Label lblPrilaku = new Label(hasilkinerja.getPrilaku().toString());
				Label lblTanggungJawab = new Label(hasilkinerja.getTanggung_jawab().toString());
				Label lblInisiatif = new Label(hasilkinerja.getInisiatif().toString());
				Label lblKerjaSama = new Label(hasilkinerja.getKerja_sama().toString());
				Label lblDisiplin = new Label(hasilkinerja.getDisiplin().toString());
				Label lblTahun = new Label(hasilkinerja.getTahun().toString());
				Label lblStatusKerja = new Label(hasilkinerja.getStatus_kinerja().toString());
				
				CustomeUtil.setObjectToListCell(li, lblPendidikan, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblKehadiran, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblPrilaku, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblTanggungJawab, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblInisiatif, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblKerjaSama, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblDisiplin, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblTahun, "text-align:center");
				
				if (hasilkinerja.getStatus_kinerja().toString().equalsIgnoreCase("Kinerja Sangat Tinggi")) {
					CustomeUtil.setObjectToListCell(li, lblStatusKerja, "text-align:center; background-color:#008080; color:#FFFFFF");
				} else if (hasilkinerja.getStatus_kinerja().toString().equalsIgnoreCase("Kinerja Tinggi")) {
					CustomeUtil.setObjectToListCell(li, lblStatusKerja, "text-align:center; background-color:#4682B4; color:#FFFFFF");
				} else if (hasilkinerja.getStatus_kinerja().toString().equalsIgnoreCase("Kinerja Sesuai Standar")) {
					CustomeUtil.setObjectToListCell(li, lblStatusKerja, "text-align:center; background-color:#FFA07A; color:#FFFFFF");
				} else if (hasilkinerja.getStatus_kinerja().toString().equalsIgnoreCase("Kinerja Rendah")) {
					CustomeUtil.setObjectToListCell(li, lblStatusKerja, "text-align:center; background-color:#DDA0DD; color:#FFFFFF");
				} else if (hasilkinerja.getStatus_kinerja().toString().equalsIgnoreCase("Kinerja Tidak Efektif")) {
					CustomeUtil.setObjectToListCell(li, lblStatusKerja, "text-align:center; background-color:#FA8072; color:#FFFFFF");
				}
				
				li.setValue(hasilkinerja);
				}
			});
		}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onOpen$bdxNip() throws SQLException {
		txtSearchNip.focus();
		modelKelPeg = new ListModelList<KelolaPegawaiDTO>(kelPegDAO.findAll());
		lsNip = modelKelPeg;
		lbxNip.setModel(new ListModelList(lsNip, true));
	}

	public void onSelect$lbxNip() {
		Listitem li = lbxNip.getSelectedItem();
		String strValue = "";
		for (Object cell : ((Listitem) li).getChildren()) {
			if (((Listcell) cell).getListheader() != null) {
				if (((Listcell) cell).getListheader().isVisible()) {
					strValue += ((Listcell) cell).getLabel() + ";";
				}
			}
		}
		String[] parts = strValue.split(";");
		nip = parts[0];
		nama = parts[1];

		System.out.println("===========>> HAHAHA 3: " + nip);
		System.out.println("===========>> HAHAHA 4: " + nama);
		
		bdxNip.setValue(nip.toString());
		txtNamaPegawai.setValue(nama.toString());
		bdxNip.close();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onClick$btnCariNip(){
		String search = txtSearchNip.getValue().trim();
		modelKelPeg = new ListModelList<KelolaPegawaiDTO>(kelPegDAO.findSearch(search));
		lsNip = modelKelPeg;
		lbxNip.setModel(new ListModelList(lsNip, true));
	}
	
	public void onOK$txtSearchNip(){
		onClick$btnCariNip();
	}
	
	private boolean isValidate() {
		Boolean bol = true;
		String strMessage = "";

		if (bdxNip.getValue().isEmpty()) {
			strMessage += "Mandatory harus diisikan";
		}

		if (!strMessage.isEmpty()) {
			alert(strMessage);
			bol = false;
		}
		return bol;
	}
}
