package model.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import connection.DbConnection;

public class ImportFileDAO {

	Connection con;
	PreparedStatement ps = null;

	public ImportFileDAO(Connection conn) {
		this.con = conn;

	}

	@SuppressWarnings("resource")
	public void importExcel(InputStream inpStream) throws SQLException, IOException, InvalidFormatException {

		Connection dbConnec = null;
		Statement state = null;

		try {
			// FileInputStream input = new
			// FileInputStream("D://datasetimport.xlsx");
			// System.out.println("====================>>" + input);
			 POIFSFileSystem fs = new POIFSFileSystem(inpStream);
			 HSSFWorkbook wb = new HSSFWorkbook(fs);
			 HSSFSheet sheet = wb.getSheetAt(0);
			 Row row;

//			OPCPackage pkg = OPCPackage.open(new File("D://datasetimport.xlsx"));
//			XSSFWorkbook wb = new XSSFWorkbook(pkg);
//			XSSFSheet sheet = wb.getSheetAt(0);
//			XSSFRow row;
//			CellReference cr = new CellReference("A1");
//			row = sheet.getRow(cr.getCol());

			dbConnec = DbConnection.getDBConnection();
			state = dbConnec.createStatement();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				String nip = row.getCell(0).getStringCellValue();
				int year = Calendar.getInstance().get(Calendar.YEAR);
//				int year = 2017;
				Integer tahun = year;
				Integer pendidikan = (int) row.getCell(1).getNumericCellValue();
				Integer kehadiran = (int) row.getCell(2).getNumericCellValue();
				Integer prilaku = (int) row.getCell(3).getNumericCellValue();
				Integer tanggungJawab = (int) row.getCell(4).getNumericCellValue();
				Integer inisiatif = (int) row.getCell(5).getNumericCellValue();
				Integer kerjaSama = (int) row.getCell(6).getNumericCellValue();
				Integer disiplin = (int) row.getCell(7).getNumericCellValue();

				double dbPendidikan = new Double(pendidikan) * 0.23;
				BigDecimal bgPendidikan = new BigDecimal(dbPendidikan);
				BigDecimal bgKehadiran = new BigDecimal(kehadiran).multiply(new BigDecimal(0.23));
				BigDecimal bgPrilaku = new BigDecimal(prilaku).multiply(new BigDecimal(0.12));
				BigDecimal bgTanggungJawab = new BigDecimal(tanggungJawab).multiply(new BigDecimal(0.12));
				BigDecimal bgInisiatif = new BigDecimal(inisiatif).multiply(new BigDecimal(0.07));
				BigDecimal bgKerjaSama = new BigDecimal(kerjaSama).multiply(new BigDecimal(0.07));
				BigDecimal bgDisiplin = new BigDecimal(disiplin).multiply(new BigDecimal(0.12));

				BigDecimal sumAllNilai = bgPendidikan.add(bgKehadiran).add(bgPrilaku).add(bgTanggungJawab)
						.add(bgInisiatif).add(bgKerjaSama).add(bgDisiplin);

				String statusKinerja = "";
				if (sumAllNilai.compareTo(new BigDecimal(4.20)) == 1) {
					statusKinerja = "Kinerja Sangat Tinggi";
				} else if (sumAllNilai.compareTo(new BigDecimal(3.40)) == 1) {
					statusKinerja = "Kinerja Tinggi";
				} else if (sumAllNilai.compareTo(new BigDecimal(2.60)) == 1) {
					statusKinerja = "Kinerja Sesuai Standar";
				} else if (sumAllNilai.compareTo(new BigDecimal(1.80)) == 1) {
					statusKinerja = "Kinerja Rendah";
				} else if (sumAllNilai.compareTo(new BigDecimal(1.00)) == 1) {
					statusKinerja = "Kinerja Tidak Efektif";
				}

				String nipAtasan = "ATASAN";

				String insUpdTbl = "insert into penilaian_kinerja"
						+ "(nip, tahun, pendidikan, kehadiran, prilaku, tanggung_jawab, inisiatif, kerja_sama, disiplin, jumlah_nilai, status_kinerja, nip_atasan) "
						+ "VALUES" + "('" + nip + "','" + tahun + "','" + pendidikan + "','" + kehadiran + "','"
						+ prilaku + "'," + "'" + tanggungJawab + "','" + inisiatif + "','" + kerjaSama + "', '"
						+ disiplin + "','" + sumAllNilai + "','" + statusKinerja + "','" + nipAtasan + "')";
				System.out.println("Import rows " + i);
				state.executeUpdate(insUpdTbl);

			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (state != null) {
				state.close();
			}

			if (dbConnec != null) {
				dbConnec.close();
			}
		}
	}

}
