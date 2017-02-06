package model.dao;

import java.util.List;
import model.dto.JumlahKinerjaPegawaiDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JumlahKinerjaPegawaiDAO {

	Connection con;

	public JumlahKinerjaPegawaiDAO(Connection conn) {
		this.con = conn;

	}
	
	public List<JumlahKinerjaPegawaiDTO> findAll(String statusKinerja, String tahun) {
        List<JumlahKinerjaPegawaiDTO> allDataKinerjaPegawai = new ArrayList<JumlahKinerjaPegawaiDTO>();
        int inTahun = 0;
        if (!tahun.isEmpty()) {
        	inTahun = Integer.parseInt(tahun);
		}
        String query = "";
        if (!statusKinerja.isEmpty() && !tahun.isEmpty()) {
        	query = "SELECT COUNT(CASE WHEN status_kinerja = 'Kinerja Sangat Tinggi' THEN 1 ELSE NULL END) AS kinerja_sangat_tinggi "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Tinggi' THEN 1ELSE NULL END) AS kinerja_tinggi "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Sesuai Standar' THEN 1 ELSE NULL END) AS kinerja_sesuai_standar "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Rendah' THEN 1 ELSE NULL END) AS kinerja_rendah "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Tidak Efektif' THEN 1 ELSE NULL END) AS kinerja_tidak_efektif "
					+ ",COUNT(*) AS all_status_kinerja FROM penilaian_kinerja "
					+ "WHERE tahun = '"+inTahun+"' AND status_kinerja = '"+statusKinerja+"'";
        	
		} else if (!statusKinerja.isEmpty()) {
			query = "SELECT COUNT(CASE WHEN status_kinerja = 'Kinerja Sangat Tinggi' THEN 1 ELSE NULL END) AS kinerja_sangat_tinggi "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Tinggi' THEN 1ELSE NULL END) AS kinerja_tinggi "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Sesuai Standar' THEN 1 ELSE NULL END) AS kinerja_sesuai_standar "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Rendah' THEN 1 ELSE NULL END) AS kinerja_rendah "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Tidak Efektif' THEN 1 ELSE NULL END) AS kinerja_tidak_efektif "
					+ ",COUNT(*) AS all_status_kinerja FROM penilaian_kinerja "
					+ "WHERE status_kinerja = '"+statusKinerja+"'";
			
		} else if (!tahun.isEmpty()) {
			query = "SELECT COUNT(CASE WHEN status_kinerja = 'Kinerja Sangat Tinggi' THEN 1 ELSE NULL END) AS kinerja_sangat_tinggi "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Tinggi' THEN 1ELSE NULL END) AS kinerja_tinggi "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Sesuai Standar' THEN 1 ELSE NULL END) AS kinerja_sesuai_standar "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Rendah' THEN 1 ELSE NULL END) AS kinerja_rendah "
					+ ",COUNT(CASE WHEN status_kinerja = 'Kinerja Tidak Efektif' THEN 1 ELSE NULL END) AS kinerja_tidak_efektif "
					+ ",COUNT(*) AS all_status_kinerja FROM penilaian_kinerja "
					+ "WHERE tahun = '"+inTahun+"'";
			
		} else {
			query = "SELECT COUNT(CASE WHEN a.status_kinerja = 'Kinerja Sangat Tinggi' THEN 1 ELSE NULL END) AS kinerja_sangat_tinggi "
					+ ",COUNT(CASE WHEN a.status_kinerja = 'Kinerja Tinggi' THEN 1ELSE NULL END) AS kinerja_tinggi "
					+ ",COUNT(CASE WHEN a.status_kinerja = 'Kinerja Sesuai Standar' THEN 1 ELSE NULL END) AS kinerja_sesuai_standar "
					+ ",COUNT(CASE WHEN a.status_kinerja = 'Kinerja Rendah' THEN 1 ELSE NULL END) AS kinerja_rendah "
					+ ",COUNT(CASE WHEN a.status_kinerja = 'Kinerja Tidak Efektif' THEN 1 ELSE NULL END) AS kinerja_tidak_efektif "
					+ ",COUNT(*) AS all_status_kinerja FROM penilaian_kinerja a left join master_pegawai b on a.nip=b.nip";
		}
       
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            JumlahKinerjaPegawaiDTO jmlKinPegDTO;
            int rowCount1 = 0;
            while (rs.next()) {
            	 rowCount1 = rs.getInt("rowcount1");
            	 
				System.out.println("=========>> rowCount1 1: " + rowCount1);
            }
        } catch (SQLException | NullPointerException x) {
            x.getMessage();
        }
//        System.out.println("=========>> rowCount1 2: " + rowCount1);
        return allDataKinerjaPegawai;
    }
	
}
