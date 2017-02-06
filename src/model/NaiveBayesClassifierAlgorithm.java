package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class NaiveBayesClassifierAlgorithm extends GenericForwardComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}

	public static void main(String[] args) {

		String dataClass = "STATUS_KELULUSAN"; // buat classData(field hasilnya)

		// get data list & kasusnya
		 List<Map<String, Object>> listData = isidata();
//		List<Map<String, Object>> listData = isidata2();
		 Map<String, Object> kasus = setKasus();
//		Map<String, Object> kasus = setKasus2();

		// convert listmap to liststring
		Map<String, Object> dataToList = (Map<String, Object>) convertToListData(listData, kasus, dataClass);
		System.out.println("datatolist : " + dataToList);

		// get class per label
		List<Map<String, Object>> listClassPerLabel = getClassPerLabel(listData, dataClass);
		System.out.println("listClassPerLabel : " + listClassPerLabel);

		// get data count kasus berdasarkan classdatanya
		List<Map<String, Object>> listCountKasus = countKasusWithClassData(dataToList, listClassPerLabel, kasus);
		System.out.println("listCountKasus : " + listCountKasus);

		// count Hasil
		List<Map<String, Object>> listHasil = countHasil(listCountKasus, listClassPerLabel);
		System.out.println("listHasil : " + listHasil);

		// get Hasilnya
		Map<String, Object> hasil = getHasil(listHasil);
		System.out.println("HASILNYA : " + hasil);

		// List<String> listtest = new ArrayList<>();
		// for(Map<String, Object> data : listData){
		// listtest.add((String) data.get("JENIS_KELAMIN"));
		// }
		// System.out.println("\nExample 2 - Count all with frequency");
		// Set<String> uniqueSet = new HashSet<String>(listtest);
		// for (String temp : uniqueSet) {
		// System.out.println(temp + ": " + Collections.frequency(listtest,
		// temp));
		// }
	}

	// TAHAP 1
	// hitung jumlah class/label (hasil akhir)
	public static List<Map<String, Object>> getClassPerLabel(List<Map<String, Object>> listData, String dataClass) {
		List<Map<String, Object>> listClassPerLabel = new ArrayList<>();
		Map<String, Object> dataClassPerLabel = new LinkedHashMap<>();
		int all = listData.size();

		// get data from class jadi listofDataClass
		List<String> listOfDataClass = new ArrayList<>();
		for (Map<String, Object> data : listData) {
			listOfDataClass.add((String) data.get(dataClass));
		}

		// get unik value from listofdataClass
		Set<String> uniqueSet = new HashSet<String>(listOfDataClass);
		for (String temp : uniqueSet) {
			int count = Collections.frequency(listOfDataClass, temp);

			// BigDecimal val = new BigDecimal(count).divide(new
			// BigDecimal(all), 8, RoundingMode.CEILING);
			BigDecimal val = divide(new BigDecimal(count), new BigDecimal(all));

			dataClassPerLabel = new LinkedHashMap<>();
			dataClassPerLabel.put("KEY", temp);
			dataClassPerLabel.put("VALUE", val);
			dataClassPerLabel.put("COUNT", count);
			dataClassPerLabel.put("RUMUS", count + "/" + all);
			dataClassPerLabel.put("LABEL", "P(Y=" + temp + ")=" + count + "/" + all);
			listClassPerLabel.add(dataClassPerLabel);
		}

		return listClassPerLabel;
	}

	// TAHAP 2
	// count jumlah kasus berdasarkan classdata nya
	public static List<Map<String, Object>> countKasusWithClassData(Map<String, Object> dataToList,
			List<Map<String, Object>> listClassPerLabel, Map<String, Object> kasus) {
		List<Map<String, Object>> listNewData = new ArrayList<>();
		Map<String, Object> newdata = new LinkedHashMap<>();

		for (String key : kasus.keySet()) {
			for (Map<String, Object> ClassPerLabel : listClassPerLabel) {
				// get count dari class per label
				int count = (int) ClassPerLabel.get("COUNT");

				// get count dari list data yang udah di convert
				int getcount = Collections.frequency((Collection<?>) dataToList.get(key),
						kasus.get(key) + "-" + ClassPerLabel.get("KEY"));

				// BigDecimal val = new BigDecimal(getcount).divide(new
				// BigDecimal(count), 8, RoundingMode.CEILING);
				BigDecimal val = divide(new BigDecimal(getcount), new BigDecimal(count));

				newdata = new LinkedHashMap<>();
				// newdata.put("KEY", key + "-" + ClassPerLabel.get("KEY"));
				newdata.put("KEY", key);
				newdata.put("CLASS", ClassPerLabel.get("KEY"));
				newdata.put("COUNT", getcount);
				newdata.put("VALUE", val);
				newdata.put("RUMUS", getcount + "/" + count);
				newdata.put("LABEL", "P(" + key + "=" + kasus.get(key) + " | " + "Y=" + ClassPerLabel.get("KEY")
						+ ") = " + getcount + "/" + count);
				listNewData.add(newdata);
			}
		}

		return listNewData;
	}

	// TAHAP 3
	// Count Hasil
	public static List<Map<String, Object>> countHasil(List<Map<String, Object>> listCountKasus,
			List<Map<String, Object>> listClassPerLabel) {
		List<Map<String, Object>> listDataResult = new ArrayList<>();
		Map<String, Object> dataResult = new LinkedHashMap<>();

		for (Map<String, Object> classPerLabel : listClassPerLabel) {
			BigDecimal hasil = new BigDecimal(1);
			String JenisClassLabel = (String) classPerLabel.get("KEY");
			String rumus = "{" + classPerLabel.get("KEY") + " = ";
			for (Map<String, Object> count : listCountKasus) {
				if (count.get("CLASS").equals(JenisClassLabel)) {
					rumus = rumus + count.get("RUMUS") + " * ";
					hasil = multiply(hasil, (BigDecimal) count.get("VALUE"));
				}
			}
			rumus = rumus + classPerLabel.get("RUMUS") + "}";
			hasil = multiply(hasil, (BigDecimal) classPerLabel.get("VALUE"));

			dataResult = new LinkedHashMap<>();
			dataResult.put("CLASS", classPerLabel.get("KEY"));
			dataResult.put("HASIL", hasil.setScale(8, RoundingMode.CEILING));
			dataResult.put("RUMUS", rumus);
			listDataResult.add(dataResult);
		}
		return listDataResult;
	}

	public static Map<String, Object> getHasil(List<Map<String, Object>> listHasil) {
		Map<String, Object> result = new LinkedHashMap<>();

		BigDecimal maxbig = new BigDecimal(0);
		String hasil = "";
		for (Map<String, Object> data : listHasil) {
			if (maxbig.compareTo((BigDecimal) data.get("HASIL")) == -1) {
				maxbig = (BigDecimal) data.get("HASIL");
				hasil = (String) data.get("CLASS");
			}
		}

		result.put("HASIL", hasil);
		result.put("VALUE", maxbig);
		return result;
	}

	// set kasus kecuali field hasil
	public static Map<String, Object> setKasus() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "5.70");
		return data;
	}

	// convert data dari list map jadi list string sesuai kasus
	public static Map<String, Object> convertToListData(List<Map<String, Object>> listData, Map<String, Object> kasus,
			String dataClass) {
		Map<String, Object> dataToList = new LinkedHashMap<>();

		for (String key : kasus.keySet()) {
			List<String> listOfData = new ArrayList<>();
			for (Map<String, Object> data : listData) {
				listOfData.add((String) data.get(key) + "-" + data.get(dataClass));
			}
			dataToList.put(key, listOfData);
		}
		return dataToList;
	}

	// function pembagi bigdecimal
	public static BigDecimal divide(BigDecimal penyebut, BigDecimal pembilang) {
		int rounding = 8; // ketilitian perhitungan (koma)
		BigDecimal hasil = new BigDecimal(0);

		if (pembilang.compareTo(new BigDecimal(0)) == 0)
			return hasil;
		if (penyebut.compareTo(new BigDecimal(0)) == 0)
			return hasil;

		hasil = penyebut.divide(pembilang, rounding, RoundingMode.CEILING);

		return hasil;
	}

	public static BigDecimal multiply(BigDecimal A, BigDecimal B) {
		BigDecimal hasil = new BigDecimal(0);

		if (A.compareTo(new BigDecimal(0)) == 0)
			return hasil;
		if (B.compareTo(new BigDecimal(0)) == 0)
			return hasil;

		hasil = A.multiply(B);

		return hasil;
	}

	public static List<Map<String, Object>> isidata() {
		List<Map<String, Object>> listData = new ArrayList<>();
		Map<String, Object> data = new LinkedHashMap<>();

		// data1
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "3.17");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data2
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "3.30");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data3
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "3.01");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data4
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "3.25");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data5
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "3.20");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data6
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "2.50");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		// data7
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "3.00");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		// data8
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "2.70");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		// data9
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "2.40");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		// data10
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "2.50");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		// data11
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "2.50");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		// data12
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "PEREMPUAN");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "3.50");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data13
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "BEKERJA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "3.30");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data14
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "MENIKAH");
		data.put("IPK_SEMETER", "3.25");
		data.put("STATUS_KELULUSAN", "TEPAT");
		listData.add(data);

		// data15
		data = new LinkedHashMap<>();
		data.put("JENIS_KELAMIN", "LAKI-LAKI");
		data.put("STATUS_MAHASISWA", "MAHASISWA");
		data.put("STATUS_PERNIKAHAN", "BELUM");
		data.put("IPK_SEMETER", "2.30");
		data.put("STATUS_KELULUSAN", "TERLAMBAT");
		listData.add(data);

		return listData;
	}

	public static List<Map<String, Object>> isidata2() {
		List<Map<String, Object>> listData = new ArrayList<>();
		Map<String, Object> data = new LinkedHashMap<>();

		// data1
		data = new LinkedHashMap<>();
		data.put("X1", "Yes");
		data.put("X2", "Single");
		data.put("X3", "125");
		data.put("CLASS", "No");
		listData.add(data);

		// data2
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Married");
		data.put("X3", "100");
		data.put("CLASS", "No");
		listData.add(data);

		// data3
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Single");
		data.put("X3", "70");
		data.put("CLASS", "No");
		listData.add(data);

		// data4
		data = new LinkedHashMap<>();
		data.put("X1", "Yes");
		data.put("X2", "Married");
		data.put("X3", "120");
		data.put("CLASS", "No");
		listData.add(data);

		// data5
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Divorce");
		data.put("X3", "95");
		data.put("CLASS", "Yes");
		listData.add(data);

		// data6
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Married");
		data.put("X3", "60");
		data.put("CLASS", "No");
		listData.add(data);

		// data7
		data = new LinkedHashMap<>();
		data.put("X1", "Yes");
		data.put("X2", "Divorce");
		data.put("X3", "220");
		data.put("CLASS", "No");
		listData.add(data);

		// data8
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Single");
		data.put("X3", "85");
		data.put("CLASS", "Yes");
		listData.add(data);

		// data9
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Married");
		data.put("X3", "75");
		data.put("CLASS", "No");
		listData.add(data);

		// data10
		data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Single");
		data.put("X3", "90");
		data.put("CLASS", "Yes");
		listData.add(data);

		return listData;
	}

	public static Map<String, Object> setKasus2() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("X1", "No");
		data.put("X2", "Married");
		data.put("X3", "120");
		return data;
	}

}
