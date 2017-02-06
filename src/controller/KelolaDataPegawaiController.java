package controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connection.DbConnection;
import model.NaiveBayesClassifierAlgorithm;
import model.dao.HasilKinerjaPegawaiDAO;
import model.dao.KelolaPegawaiDAO;
import model.dao.NaiveBayesAlgorithmDAO;
import model.dto.KelolaPegawaiDTO;
import util.CustomeUtil;

public class KelolaDataPegawaiController extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Window winKelolaDataPegawai;

	Bandbox bdxNip;

	Textbox txtSearchNip;
	Textbox txtNamaPegawai;
	Textbox txtTahun;
	Textbox txtStatusKinerja;
	
	Datebox dtbTanggalInput;

	Combobox cmbPendidikan;

	Listbox lbxNip;
	
//	Decimalbox dcxValNBCAlgorithm; 
//	Decimalbox dcxJumlahNilai;
	Decimalbox dcxKehadiran;
	Decimalbox dcxPrilaku;
	Decimalbox dcxTanggungJawab;
	Decimalbox dcxInisiatif;
	Decimalbox dcxKerjaSama;
	Decimalbox dcxDisiplin;

	Button btnCariNip;
	Button btnView;
	Button btnSave;
	Button btnClear;
	
	Label lblTahap0Pendidikan;
	Label lblTahap0Kehadiran;
	Label lblTahap0Prilaku;
	Label lblTahap0TanggungJawab;
	Label lblTahap0Inisiatif;
	Label lblTahap0KerjaSama;
	Label lblTahap0Disiplin;
	
	Label lblTahap1KinerjaTE;
	Label lblTahap1KinerjaSS;
	Label lblTahap1KinerjaT;
	Label lblTahap1KinerjaR;
	Label lblTahap1KinerjaST;
	
	Label lblTahap2Pendidikan;
	Label lblTahap2Kehadiran;
	Label lblTahap2Prilaku;
	Label lblTahap2TanggungJawab;
	Label lblTahap2Inisiatif;
	Label lblTahap2KerjaSama;
	Label lblTahap2Disiplin;
	
	Label lblTahap3KinerjaTE;
	Label lblTahap3KinerjaSS;
	Label lblTahap3KinerjaT;
	Label lblTahap3KinerjaR;
	Label lblTahap3KinerjaST;
	
	Label lblHasil;
	
	private List<KelolaPegawaiDTO> lsNip;

	KelolaPegawaiDAO kelPegDAO = new KelolaPegawaiDAO(DbConnection.getDBConnection());
	NaiveBayesAlgorithmDAO nbcAlgortithmDAO = new NaiveBayesAlgorithmDAO(DbConnection.getDBConnection());
	
	ListModelList<KelolaPegawaiDTO> model = new ListModelList<KelolaPegawaiDTO>();

	HasilKinerjaPegawaiDAO hslKinPegDAO = new HasilKinerjaPegawaiDAO(DbConnection.getDBConnection());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initComponents();
	}

	private void initComponents() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		txtTahun.setValue(Integer.toString(year));
		dtbTanggalInput.setValue(new Date());
		getValCmbPendidikan();
		btnSave.setDisabled(true);
	}

	private void getValCmbPendidikan() {
		Map<String, Object> lstPendidikan = new LinkedHashMap<String, Object>();
		lstPendidikan.put("1", "SMA");
		lstPendidikan.put("2", "D3");
		lstPendidikan.put("3", "S1");
		CustomeUtil.populateComboDecisionId(cmbPendidikan, lstPendidikan, null, true, true);
	}

	public void onClick$btnClear() {
		bdxNip.setValue("");
		// txtSearchNip.setValue("");
		txtNamaPegawai.setValue("");
		txtStatusKinerja.setValue("");
		cmbPendidikan.setSelectedIndex(0);
//		dcxValNBCAlgorithm.setValue(BigDecimal.ZERO);
		dcxKehadiran.setValue(BigDecimal.ZERO);
		dcxPrilaku.setValue(BigDecimal.ZERO);
		dcxTanggungJawab.setValue(BigDecimal.ZERO);
		dcxInisiatif.setValue(BigDecimal.ZERO);
		dcxKerjaSama.setValue(BigDecimal.ZERO);
		dcxDisiplin.setValue(BigDecimal.ZERO);
		btnSave.setDisabled(true);
		disableFalseComponents();
	}
	
	public void disableFalseComponents() {
		bdxNip.setDisabled(false);
		cmbPendidikan.setDisabled(false);
		dcxKehadiran.setReadonly(false);
		dcxPrilaku.setReadonly(false);
		dcxTanggungJawab.setReadonly(false);
		dcxInisiatif.setReadonly(false);
		dcxKerjaSama.setReadonly(false);
		dcxDisiplin.setReadonly(false);
		btnView.setDisabled(false);
	}
	
	public void disableTrueComponents() {
		bdxNip.setDisabled(true);
		// txtSearchNip.setValue("");
		txtNamaPegawai.setReadonly(true);
		txtStatusKinerja.setReadonly(true);
		cmbPendidikan.setDisabled(true);
//		dcxJumlahNilai.setReadonly(true);
		dcxKehadiran.setReadonly(true);
		dcxPrilaku.setReadonly(true);
		dcxTanggungJawab.setReadonly(true);
		dcxInisiatif.setReadonly(true);
		dcxKerjaSama.setReadonly(true);
		dcxDisiplin.setReadonly(true);
		btnView.setDisabled(true);
	}

	public void onClick$dcxKehadiran() {
		dcxKehadiran.setSelectionRange(0, 50);
	}

	public void onChange$dcxKehadiran() {
		if (dcxKehadiran.getValue() == null) {
			dcxKehadiran.setValue(BigDecimal.ZERO);
		}
		if (dcxKehadiran.getValue().compareTo(BigDecimal.ZERO) == -1) {
			dcxKehadiran.setValue(BigDecimal.ZERO);
		}
		if (dcxKehadiran.getValue().compareTo(new BigDecimal(3)) == 1) {
			dcxKehadiran.setValue(BigDecimal.ZERO);
		}
	}

	public void onClick$dcxPrilaku() {
		dcxPrilaku.setSelectionRange(0, 50);
	}

	public void onChange$dcxPrilaku() {
		if (dcxPrilaku.getValue() == null) {
			dcxPrilaku.setValue(BigDecimal.ZERO);
		}
		if (dcxPrilaku.getValue().compareTo(BigDecimal.ZERO) == -1) {
			dcxPrilaku.setValue(BigDecimal.ZERO);
		}
		if (dcxPrilaku.getValue().compareTo(new BigDecimal(5)) == 1) {
			dcxPrilaku.setValue(BigDecimal.ZERO);
		}
	}

	public void onClick$dcxTanggungJawab() {
		dcxTanggungJawab.setSelectionRange(0, 50);
	}

	public void onChange$dcxTanggungJawab() {
		if (dcxTanggungJawab.getValue() == null) {
			dcxTanggungJawab.setValue(BigDecimal.ZERO);
		}
		if (dcxTanggungJawab.getValue().compareTo(BigDecimal.ZERO) == -1) {
			dcxTanggungJawab.setValue(BigDecimal.ZERO);
		}
		if (dcxTanggungJawab.getValue().compareTo(new BigDecimal(5)) == 1) {
			dcxTanggungJawab.setValue(BigDecimal.ZERO);
		}
	}

	public void onClick$dcxInisiatif() {
		dcxInisiatif.setSelectionRange(0, 50);
	}

	public void onChange$dcxInisiatif() {
		if (dcxInisiatif.getValue() == null) {
			dcxInisiatif.setValue(BigDecimal.ZERO);
		}
		if (dcxInisiatif.getValue().compareTo(BigDecimal.ZERO) == -1) {
			dcxInisiatif.setValue(BigDecimal.ZERO);
		}
		if (dcxInisiatif.getValue().compareTo(new BigDecimal(5)) == 1) {
			dcxInisiatif.setValue(BigDecimal.ZERO);
		}
	}

	public void onClick$dcxKerjaSama() {
		dcxKerjaSama.setSelectionRange(0, 50);
	}

	public void onChange$dcxKerjaSama() {
		if (dcxKerjaSama.getValue() == null) {
			dcxKerjaSama.setValue(BigDecimal.ZERO);
		}
		if (dcxKerjaSama.getValue().compareTo(BigDecimal.ZERO) == -1) {
			dcxKerjaSama.setValue(BigDecimal.ZERO);
		}
		if (dcxKerjaSama.getValue().compareTo(new BigDecimal(5)) == 1) {
			dcxKerjaSama.setValue(BigDecimal.ZERO);
		}
	}

	public void onClick$dcxDisiplin() {
		dcxDisiplin.setSelectionRange(0, 50);
	}

	public void onChange$dcxDisiplin() {
		if (dcxDisiplin.getValue() == null) {
			dcxDisiplin.setValue(BigDecimal.ZERO);
		}
		if (dcxDisiplin.getValue().compareTo(BigDecimal.ZERO) == -1) {
			dcxDisiplin.setValue(BigDecimal.ZERO);
		}
		if (dcxDisiplin.getValue().compareTo(new BigDecimal(5)) == 1) {
			dcxDisiplin.setValue(BigDecimal.ZERO);
		}
	}

	public void onClick$btnView() {
		if (isValidate()) {
			getNilai();
			btnSave.setDisabled(false);
			disableTrueComponents();
		}
	}

	public void onClick$btnSave() {
		if (isValidate()) {
			try {
				getNilai();
				prosesInsert();
				onClick$btnClear();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void getNilai() {
		String statusKinerja = "";
		String tahun = "";

		String dataClass = "STATUS_KINERJA";

		List<Map<String, Object>> listData = nbcAlgortithmDAO.findByNaiveBayesAlgoritm(statusKinerja, tahun);
		Map<String, Object> kasus = getValComponents();

		Map<String, Object> dataToList = (Map<String, Object>) NaiveBayesClassifierAlgorithm.convertToListData(listData, kasus, dataClass);
		System.out.println("datatolist : " + dataToList);
		
		String pendidikan0 = dataToList.get("PENDIDIKAN").toString();
		lblTahap0Pendidikan.setValue(pendidikan0);
		String kehadiran0 = dataToList.get("KEHADIRAN").toString();
		lblTahap0Kehadiran.setValue(kehadiran0);
		String prilaku0 = dataToList.get("PRILAKU").toString();
		lblTahap0Prilaku.setValue(prilaku0);
		String tanggungJawab0 = dataToList.get("TANGGUNG_JAWAB").toString();
		lblTahap0TanggungJawab.setValue(tanggungJawab0);
		String inisiatif0 = dataToList.get("INISIATIF").toString();
		lblTahap0Inisiatif.setValue(inisiatif0);
		String kerjaSama0 = dataToList.get("KERJA_SAMA").toString();
		lblTahap0KerjaSama.setValue(kerjaSama0);
		String disiplin0 = dataToList.get("DISIPLIN").toString();
		lblTahap0Disiplin.setValue(disiplin0);
		
//		System.err.println("ZZZ"+a);
		// get class per label
		List<Map<String, Object>> listClassPerLabel = NaiveBayesClassifierAlgorithm.getClassPerLabel(listData, dataClass);
		System.out.println("listClassPerLabel : " + listClassPerLabel);
		
		lblTahap1KinerjaTE.setValue(listClassPerLabel.get(0).toString());
		lblTahap1KinerjaSS.setValue(listClassPerLabel.get(1).toString());
		lblTahap1KinerjaT.setValue(listClassPerLabel.get(2).toString());
		lblTahap1KinerjaR.setValue(listClassPerLabel.get(3).toString());
		lblTahap1KinerjaST.setValue(listClassPerLabel.get(4).toString());

		// get data count kasus berdasarkan classdatanya
		List<Map<String, Object>> listCountKasus = NaiveBayesClassifierAlgorithm.countKasusWithClassData(dataToList, listClassPerLabel, kasus);
		System.out.println("listCountKasus : " + listCountKasus);
		lblTahap2Pendidikan.setValue("1."+listCountKasus.get(0).toString()+" 2."+listCountKasus.get(1).toString()
				+" 3."+listCountKasus.get(2).toString() +" 4."+listCountKasus.get(3).toString() +" 5."+listCountKasus.get(4).toString());
		lblTahap2Kehadiran.setValue("1."+listCountKasus.get(5).toString()+" 2."+listCountKasus.get(6).toString()
				+" 3."+listCountKasus.get(7).toString() +" 4."+listCountKasus.get(8).toString() +" 5."+listCountKasus.get(9).toString());
		lblTahap2Prilaku.setValue("1."+listCountKasus.get(10).toString()+" 2."+listCountKasus.get(11).toString()
				+" 3."+listCountKasus.get(12).toString() +" 4."+listCountKasus.get(13).toString() +" 5."+listCountKasus.get(14).toString());
		lblTahap2TanggungJawab.setValue("1."+listCountKasus.get(15).toString()+" 2."+listCountKasus.get(16).toString()
				+" 3."+listCountKasus.get(17).toString() +" 4."+listCountKasus.get(18).toString() +" 5."+listCountKasus.get(19).toString());
		lblTahap2Inisiatif.setValue("1."+listCountKasus.get(20).toString()+" 2."+listCountKasus.get(21).toString()
				+" 3."+listCountKasus.get(22).toString() +" 4."+listCountKasus.get(23).toString() +" 5."+listCountKasus.get(24).toString());
		lblTahap2KerjaSama.setValue("1."+listCountKasus.get(25).toString()+" 2."+listCountKasus.get(26).toString()
				+" 3."+listCountKasus.get(27).toString() +" 4."+listCountKasus.get(28).toString() +" 5."+listCountKasus.get(29).toString());
		lblTahap2Disiplin.setValue("1."+listCountKasus.get(30).toString()+" 2."+listCountKasus.get(31).toString()
				+" 3."+listCountKasus.get(32).toString() +" 4."+listCountKasus.get(33).toString() +" 5."+listCountKasus.get(34).toString());
		
		
		// count Hasil
		List<Map<String, Object>> listHasil = NaiveBayesClassifierAlgorithm.countHasil(listCountKasus, listClassPerLabel);
		System.out.println("listHasil : " + listHasil);
		lblTahap3KinerjaTE.setValue(listHasil.get(0).toString());
		lblTahap3KinerjaSS.setValue(listHasil.get(1).toString());
		lblTahap3KinerjaT.setValue(listHasil.get(2).toString());
		lblTahap3KinerjaR.setValue(listHasil.get(3).toString());
		lblTahap3KinerjaST.setValue(listHasil.get(4).toString());

		// get Hasilnya
		Map<String, Object> hasil = NaiveBayesClassifierAlgorithm.getHasil(listHasil);
		System.out.println("HASILNYA : " + hasil);

		txtStatusKinerja.setValue((String) hasil.get("HASIL"));
		lblHasil.setValue(hasil.toString());
		
		BigDecimal valNbc = (BigDecimal) hasil.get("VALUE");
//		dcxValNBCAlgorithm.setValue(valNbc);
		System.out.println("=====================>>> 1 :" +hasil.get("HASIL"));
		System.out.println("=====================>>> 2 :" +hasil.get("VALUE"));
		
		double dbPendidikan = new Double(cmbPendidikan.getSelectedItem().getValue()) * 0.23;
		BigDecimal bgPendidikan = new BigDecimal(dbPendidikan);
		BigDecimal bgKehadiran = dcxKehadiran.getValue().multiply(new BigDecimal(0.23));
		BigDecimal bgPrilaku = dcxPrilaku.getValue().multiply(new BigDecimal(0.12));
		BigDecimal bgTanggungJawab = dcxTanggungJawab.getValue().multiply(new BigDecimal(0.12));
		BigDecimal bgInisiatif = dcxInisiatif.getValue().multiply(new BigDecimal(0.07));
		BigDecimal bgKerjaSama = dcxKerjaSama.getValue().multiply(new BigDecimal(0.07));
		BigDecimal bgDisiplin = dcxDisiplin.getValue().multiply(new BigDecimal(0.12));

		BigDecimal sumAllNilai = bgPendidikan.add(bgKehadiran).add(bgPrilaku).add(bgTanggungJawab).add(bgInisiatif)
				.add(bgKerjaSama).add(bgDisiplin);
//		dcxJumlahNilai.setValue(sumAllNilai);
		String test = "";
		if (sumAllNilai.compareTo(new BigDecimal(4.20)) == 1) {
//			txtStatusKinerja.setValue("Kinerja Sangat Tinggi");
			test = "Kinerja Sangat Tinggi";
		} else if (sumAllNilai.compareTo(new BigDecimal(3.40)) == 1) {
//			txtStatusKinerja.setValue("Kinerja Tinggi");
			test = "Kinerja Tinggi";
		} else if (sumAllNilai.compareTo(new BigDecimal(2.60)) == 1) {
//			txtStatusKinerja.setValue("Kinerja Sesuai Standar");
			test = "Kinerja Sesuai Standar";
		} else if (sumAllNilai.compareTo(new BigDecimal(1.80)) == 1) {
//			txtStatusKinerja.setValue("Kinerja Rendah");
			test = "Kinerja Rendah";
		} else if (sumAllNilai.compareTo(new BigDecimal(1.00)) == 1) {
//			txtStatusKinerja.setValue("Kinerja Tidak Efektif");
			test = "Kinerja Tidak Efektif";
		}
		System.out.println("=====================>>> 3 :" + test);
	}
	
	public Map<String, Object> getValComponents() {
		Map<String, Object> mapData = new LinkedHashMap<>();
		mapData.put("PENDIDIKAN", cmbPendidikan.getSelectedItem().getValue().toString());
    	mapData.put("KEHADIRAN", dcxKehadiran.getValue().toString());
    	mapData.put("PRILAKU", dcxPrilaku.getValue().toString());
    	mapData.put("TANGGUNG_JAWAB", dcxTanggungJawab.getValue().toString());
    	mapData.put("INISIATIF", dcxInisiatif.getValue().toString());
    	mapData.put("KERJA_SAMA", dcxKerjaSama.getValue().toString());
    	mapData.put("DISIPLIN", dcxDisiplin.getValue().toString());
		return mapData;
	}

	private void prosesInsert() throws SQLException {
		String nip = bdxNip.getValue();
		Integer tahun = new Integer(txtTahun.getValue());
		String pendidikan = cmbPendidikan.getSelectedItem().getValue();
		Integer kehadiran = dcxKehadiran.getValue().intValue();
		Integer prilaku = dcxPrilaku.getValue().intValue();
		Integer tanggungJawab = dcxTanggungJawab.getValue().intValue();
		Integer inisiatif = dcxInisiatif.getValue().intValue();
		Integer kerjaSama = dcxKerjaSama.getValue().intValue();
		Integer disiplin = dcxDisiplin.getValue().intValue();
//		Double jumlahNilai = dcxJumlahNilai.getValue().doubleValue();
		Double jumlahNilai = new Double(0);
		String statusKinerja = txtStatusKinerja.getValue();
		String nipAtasan = "";

		KelolaPegawaiDAO kelPegDAO = new KelolaPegawaiDAO(DbConnection.getDBConnection());
		kelPegDAO.insertKelolaPegawai(nip, tahun, pendidikan, kehadiran, prilaku, tanggungJawab, inisiatif, kerjaSama,
				disiplin, jumlahNilai, statusKinerja, nipAtasan);
		CustomeUtil.infoWithTitle("Data berhasil disimpan", "Informasi");
	}

	private boolean isValidate() {
		Boolean bol = true;
		String strMessage = "";

		if (bdxNip.getValue().isEmpty() 
				|| cmbPendidikan.getSelectedItem().getValue().toString().isEmpty()
				|| dcxKehadiran.getValue().compareTo(BigDecimal.ZERO) == 0
				|| dcxTanggungJawab.getValue().compareTo(BigDecimal.ZERO) == 0
				|| dcxKerjaSama.getValue().compareTo(BigDecimal.ZERO) == 0
				|| dcxPrilaku.getValue().compareTo(BigDecimal.ZERO) == 0
				|| dcxInisiatif.getValue().compareTo(BigDecimal.ZERO) == 0
				|| dcxDisiplin.getValue().compareTo(BigDecimal.ZERO) == 0) {
			strMessage += "Mandatory harus diisikan";
		}

		if (!strMessage.isEmpty()) {
			alert(strMessage);
			bol = false;
		}
		return bol;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onOpen$bdxNip() throws SQLException {
		txtSearchNip.focus();
		model = new ListModelList<KelolaPegawaiDTO>(kelPegDAO.findAll());
		lsNip = model;
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
		String nip = parts[0];
		String nama = parts[1];

		bdxNip.setValue(nip.toString());
		txtNamaPegawai.setValue(nama.toString());
		bdxNip.close();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onClick$btnCariNip() {
		String search = txtSearchNip.getValue().trim();
		model = new ListModelList<KelolaPegawaiDTO>(kelPegDAO.findSearch(search));
		lsNip = model;
		lbxNip.setModel(new ListModelList(lsNip, true));
	}

	public void onOK$txtSearchNip() {
		onClick$btnCariNip();
	}

}
