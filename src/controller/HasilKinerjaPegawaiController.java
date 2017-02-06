package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import connection.DbConnection;
import model.dao.HasilKinerjaPegawaiDAO;
import model.dao.JumlahKinerjaPegawaiDAO;
import model.dto.HasilKinerjaPegawaiDTO;
import model.dto.JumlahKinerjaPegawaiDTO;
import util.CustomeUtil;



public class HasilKinerjaPegawaiController extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Window winHasilKinerjaPegawai;
	
	Combobox cmbKriteriaKinerja;
	Combobox cmbTahun;
	
	Button btnSearch;
	Button btnClear;
	
//	Label lblPersentaseSangatTinggi;
//	Label lblPersentaseTinggi;
//	Label lblPersentaseSesuaiStandar;
//	Label lblPersentaseRendah;
//	Label lblPersentaseTidakEfektif;
//	Label lblTotal;
	
//	Label lblJumlahKinerjaSangatTinggi;
//	Label lblJumlahKinerjaTinggi;
//	Label lblJumlahKinerjaSesuaiStandar;
//	Label lblJumlahKinerjaKinerjaRendah;
//	Label lblJumlahKinerjaTidakEfektif;
//	
//	Label lblJumlahPegawaiSangatTinggi;
//	Label lblJumlahPegawaiTinggi;
//	Label lblJumlahPegawaiSesuaiStandar;
//	Label lblJumlahPegawaiKinerjaRendah;
//	Label lblJumlahPegawaiTidakEfektif;
	
	Listbox lbxHasilKinerja;
	

	private ListModelList<HasilKinerjaPegawaiDTO> lmlHasilKinerja;
	List<HasilKinerjaPegawaiDTO> lstDataHasilKinerja;
	List<JumlahKinerjaPegawaiDTO> lstDataJumlahKinerja;
	
	HasilKinerjaPegawaiDAO hslKinPegDAO = new HasilKinerjaPegawaiDAO(DbConnection.getDBConnection());
	JumlahKinerjaPegawaiDAO jmlKinPegDAO = new JumlahKinerjaPegawaiDAO(DbConnection.getDBConnection());
	
	ListModelList<HasilKinerjaPegawaiDTO> lmlHslKinPegDTO = new ListModelList<HasilKinerjaPegawaiDTO>();
	ListModelList<JumlahKinerjaPegawaiDTO> lmlJumKinPegDTO = new ListModelList<JumlahKinerjaPegawaiDTO>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initComponents();
	}

	private void initComponents() {
		getValCmbKriteriaKinerja();
		getValCmbTahun();
	}


	private void getValCmbKriteriaKinerja() {
		Map<String, Object> lstKriteriaKinerja = new LinkedHashMap<String, Object>();
		lstKriteriaKinerja.put("1", "Kinerja Sangat Tinggi");
		lstKriteriaKinerja.put("2", "Kinerja Tinggi");
		lstKriteriaKinerja.put("3", "Kinerja Sesuai Standar");
		lstKriteriaKinerja.put("4", "Kinerja Rendah");
		lstKriteriaKinerja.put("5", "Kinerja Tidak Efektif");
		CustomeUtil.populateComboDecisionLabel(cmbKriteriaKinerja, lstKriteriaKinerja, null, true, true);
	}
	

	private void getValCmbTahun() {
		for (int thn = 2015; thn <= Integer.parseInt(getThn()); thn++) {
            cmbTahun.appendItem(String.valueOf(thn));
        }
	}
	
	public void onClick$btnClear(){
		cmbKriteriaKinerja.setSelectedIndex(0);
		cmbTahun.setValue("");
//		lblPersentaseSangatTinggi.setValue("");
//		lblPersentaseTinggi.setValue("");
//		lblPersentaseSesuaiStandar.setValue("");
//		lblPersentaseRendah.setValue("");
//		lblPersentaseTidakEfektif.setValue("");
		if (lmlHasilKinerja != null) {
			lmlHasilKinerja.clear();
			lstDataHasilKinerja.clear();
		}
	}
	
//	public void onSelect$cmbKriteriaKinerja(){
//		alert("get cmbKriteriaKinerja :" + cmbKriteriaKinerja.getSelectedItem().getValue());
//	}
//	
//	public void onSelect$cmbTahun(){
//		alert("get Tahun :" + cmbTahun.getValue());
//	}
	
	public static String getThn() {
        SimpleDateFormat sdfThn = new SimpleDateFormat("yyyy");
        Date dateThn = new Date();
        return sdfThn.format(dateThn);
    }
	
	public void onClick$btnSearch(){
		
		String statusKinerja= cmbKriteriaKinerja.getSelectedItem().getValue();
		String tahun = cmbTahun.getValue();
		
		
		lmlHslKinPegDTO = new ListModelList<HasilKinerjaPegawaiDTO>(hslKinPegDAO.findAll(statusKinerja, tahun));
		
		lmlJumKinPegDTO = new ListModelList<JumlahKinerjaPegawaiDTO>(jmlKinPegDAO.findAll(statusKinerja, tahun));
		System.out.println("================>> lsDataJumlahKinerja" + lmlJumKinPegDTO);

		String test = ""; 
		for (JumlahKinerjaPegawaiDTO jmlKPD : lmlJumKinPegDTO) {
			test = jmlKPD.getKinerja_rendah();
			break;
		}
		System.out.println("a"+test);
		lstDataHasilKinerja = lmlHslKinPegDTO;
		if (lstDataHasilKinerja.isEmpty()) {
			CustomeUtil.infoWithTitle("Data yang anda cari tidak ditemukan", "Informasi");
		}
//		lblPersentaseSangatTinggi.setValue("6.92%");
//		lblPersentaseTinggi.setValue("12.08%");
//		lblPersentaseSesuaiStandar.setValue("43.25%");
//		lblPersentaseRendah.setValue("20.13%");
//		lblPersentaseTidakEfektif.setValue("15.62%");
		renderListBox(lstDataHasilKinerja);
	}
	
	private void renderListBox(List<HasilKinerjaPegawaiDTO> lstDataHasilKinerja) {

		lmlHasilKinerja = new ListModelList<HasilKinerjaPegawaiDTO>(lstDataHasilKinerja);
		lbxHasilKinerja.setModel(lmlHasilKinerja);
		lbxHasilKinerja.setItemRenderer(new ListitemRenderer<Object>() {
			
			@Override
			public void render(Listitem li, Object data, int arg) throws Exception {
					
				
				final HasilKinerjaPegawaiDTO hasilkinerja = (HasilKinerjaPegawaiDTO) data;
				
				String strPendidikan = "";
				if (hasilkinerja.getPendidikan().toString().equals("1")) {
					strPendidikan = "SMA";
				} else if (hasilkinerja.getPendidikan().toString().equals("2")){
					strPendidikan = "D3";
				} else {
					strPendidikan = "S1";
				}
				
//				Label lblNip = new Label(hasilkinerja.get("nip").toString());
//				Label lblNama = new Label(hasilkinerja.get("nama").toString());
//				Label lblPendidikan = new Label(strPendidikan);
//				Label lblKehadiran = new Label(hasilkinerja.get("kehadiran").toString());
//				Label lblPrilaku = new Label(hasilkinerja.get("prilaku").toString());
//				Label lblTanggungJawab = new Label(hasilkinerja.get("tanggung_jawab").toString());
//				Label lblInisiatif = new Label(hasilkinerja.get("inisiatif").toString());
//				Label lblKerjaSama = new Label(hasilkinerja.get("kerja_sama").toString());
//				Label lblDisiplin = new Label(hasilkinerja.get("disiplin").toString());
//				Label lblJumlahNilai = new Label(jmlNilai.toString());
//				Label lblStatusKerja = new Label(hasilkinerja.get("status_kinerja").toString());
				
				Label lblNip = new Label(hasilkinerja.getNip().toString());
				Label lblNama = new Label(hasilkinerja.getNama().toString());
				Label lblPendidikan = new Label(strPendidikan);
				Label lblKehadiran = new Label(hasilkinerja.getKehadiran().toString());
				Label lblPrilaku = new Label(hasilkinerja.getPrilaku().toString());
				Label lblTanggungJawab = new Label(hasilkinerja.getTanggung_jawab().toString());
				Label lblInisiatif = new Label(hasilkinerja.getInisiatif().toString());
				Label lblKerjaSama = new Label(hasilkinerja.getKerja_sama().toString());
				Label lblDisiplin = new Label(hasilkinerja.getDisiplin().toString());
				Label lblTahun = new Label(hasilkinerja.getTahun().toString());
				Label lblStatusKerja = new Label(hasilkinerja.getStatus_kinerja().toString());
				
				CustomeUtil.setObjectToListCell(li, lblNip, "text-align:center");
				CustomeUtil.setObjectToListCell(li, lblNama, "text-align:left");
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
				
//				lblPersentaseSangatTinggi.setStyle("background-color:#008080; color:#FFFFFF");
//				lblPersentaseTinggi.setStyle("background-color:#4682B4; color:#FFFFFF");
//				lblPersentaseSesuaiStandar.setStyle("background-color:#FFA07A; color:#FFFFFF");
//				lblPersentaseRendah.setStyle("background-color:#DDA0DD; color:#FFFFFF");
//				lblPersentaseTidakEfektif.setStyle("background-color:#FA8072; color:#FFFFFF");
				
				li.setValue(hasilkinerja);
				}
			});
		}
}
