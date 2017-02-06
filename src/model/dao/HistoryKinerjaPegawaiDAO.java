package model.dao;

import java.util.List;

import model.dto.HasilKinerjaPegawaiDTO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HistoryKinerjaPegawaiDAO {

	Connection con;

	public HistoryKinerjaPegawaiDAO(Connection conn) {
		this.con = conn;

	}
	
	public List<HasilKinerjaPegawaiDTO> findAll(String strNip) {
        List<HasilKinerjaPegawaiDTO> allDataKinerjaPegawai = new ArrayList<HasilKinerjaPegawaiDTO>();
      
        String query = "select * from penilaian_kinerja a left join master_pegawai b on a.nip=b.nip where a.nip = '"+strNip+"'";
       
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            HasilKinerjaPegawaiDTO hslKinPegDTO;
            while (rs.next()) {
                hslKinPegDTO = new HasilKinerjaPegawaiDTO();
                hslKinPegDTO.setId(rs.getInt("id"));
				hslKinPegDTO.setNip(rs.getString("nip"));
				hslKinPegDTO.setNama(rs.getString("nama"));
				hslKinPegDTO.setTahun(rs.getInt("tahun"));
				hslKinPegDTO.setPendidikan(rs.getString("pendidikan"));
				hslKinPegDTO.setKehadiran(rs.getInt("kehadiran"));
				hslKinPegDTO.setPrilaku(rs.getInt("prilaku"));
				hslKinPegDTO.setTanggung_jawab(rs.getInt("tanggung_jawab"));
				hslKinPegDTO.setInisiatif(rs.getInt("inisiatif"));
				hslKinPegDTO.setKerja_sama(rs.getInt("kerja_sama"));
				hslKinPegDTO.setDisiplin(rs.getInt("disiplin"));
				hslKinPegDTO.setJumlah_nilai(rs.getDouble("jumlah_nilai"));
				hslKinPegDTO.setStatus_kinerja(rs.getString("status_kinerja"));
				hslKinPegDTO.setNip_atasan(rs.getString("nip_atasan"));
				allDataKinerjaPegawai.add(hslKinPegDTO);
            }
        } catch (SQLException | NullPointerException x) {
            x.getMessage();
        }
        return allDataKinerjaPegawai;
    }
}
