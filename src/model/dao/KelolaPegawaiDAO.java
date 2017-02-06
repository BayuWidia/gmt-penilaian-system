package model.dao;

import java.util.List;

import connection.DbConnection;
import model.dto.KelolaPegawaiDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class KelolaPegawaiDAO {

	Connection con;

	public KelolaPegawaiDAO(Connection conn) {
		this.con = conn;

	}
	
	public List<KelolaPegawaiDTO> findAll() {
        List<KelolaPegawaiDTO> allDataPegawai = new ArrayList<KelolaPegawaiDTO>();

        String query = "select * from master_pegawai";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            KelolaPegawaiDTO kelPegDTO;
            while (rs.next()) {
                kelPegDTO = new KelolaPegawaiDTO();
                kelPegDTO.setId(rs.getInt("id"));
				kelPegDTO.setNip(rs.getString("nip"));
				kelPegDTO.setNip_lama(rs.getString("nip_lama"));
				kelPegDTO.setNama(rs.getString("nama"));
				kelPegDTO.setStatus_karyawan(rs.getString("status_karyawan"));
				kelPegDTO.setId_jabatan(rs.getInt("id_jabatan"));
				kelPegDTO.setStatus(rs.getInt("status"));
                allDataPegawai.add(kelPegDTO);
            }
        } catch (SQLException | NullPointerException x) {
            x.getMessage();
        }
        return allDataPegawai;
    }
	
	public List<KelolaPegawaiDTO> findSearch(String search) {
        List<KelolaPegawaiDTO> allDataPegawai = new ArrayList<KelolaPegawaiDTO>();

        String query = "select * from master_pegawai where nama like '%"+search+"%'";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            KelolaPegawaiDTO kelPegDTO;
            while (rs.next()) {
                kelPegDTO = new KelolaPegawaiDTO();
                kelPegDTO.setId(rs.getInt("id"));
				kelPegDTO.setNip(rs.getString("nip"));
				kelPegDTO.setNip_lama(rs.getString("nip_lama"));
				kelPegDTO.setNama(rs.getString("nama"));
				kelPegDTO.setStatus_karyawan(rs.getString("status_karyawan"));
				kelPegDTO.setId_jabatan(rs.getInt("id_jabatan"));
				kelPegDTO.setStatus(rs.getInt("status"));
                allDataPegawai.add(kelPegDTO);
            }
        } catch (SQLException | NullPointerException x) {
            x.getMessage();
        }
        return allDataPegawai;
    }

	public void insertKelolaPegawai(String nip, Integer tahun, String pendidikan, Integer kehadiran, Integer prilaku,
			Integer tanggungJawab, Integer inisiatif, Integer kerjaSama, Integer disiplin, Double jumlahNilai,
			String statusKinerja, String nipAtasan) throws SQLException {

		Connection dbConnec = null;
		Statement state = null;
		String insUpdTbl = "insert into penilaian_kinerja"
				+ "(nip, tahun, pendidikan, kehadiran, prilaku, tanggung_jawab, inisiatif, kerja_sama, disiplin, jumlah_nilai, status_kinerja, nip_atasan) "
				+ "VALUES" + "('"+ nip + "','" + tahun + "','" + pendidikan + "','" + kehadiran + "','" + prilaku + "',"
				+ "'" + tanggungJawab + "','" + inisiatif + "','" + kerjaSama + "', '" + disiplin
				+ "','" + jumlahNilai + "','" + statusKinerja + "','" + nipAtasan + "')";

		try {
			dbConnec = DbConnection.getDBConnection();
			state = dbConnec.createStatement();

			state.executeUpdate(insUpdTbl);
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
