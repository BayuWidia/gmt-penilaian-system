package model.dao;

import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class NaiveBayesAlgorithmDAO {

	Connection con;

	public NaiveBayesAlgorithmDAO(Connection conn) {
		this.con = conn;

	}

	public List<Map<String, Object>> findByNaiveBayesAlgoritm(String statusKinerja, String tahun) {
        List<Map<String, Object>> allDataKinerjaPegawai = new ArrayList<Map<String, Object>>();
        int inTahun = 0;
        if (!tahun.isEmpty()) {
        	inTahun = Integer.parseInt(tahun);
		}
        String query = "";
        if (!statusKinerja.isEmpty() && !tahun.isEmpty()) {
        	query = "select * from penilaian_kinerja a left join master_pegawai b on a.nip=b.nip where status_kinerja = '"+statusKinerja+"' and tahun = '"+inTahun+"'";
		} else if (!statusKinerja.isEmpty()) {
			query = "select * from penilaian_kinerja a left join master_pegawai b on a.nip=b.nip where status_kinerja = '"+statusKinerja+"'";
		} else if (!tahun.isEmpty()) {
			query = "select * from penilaian_kinerja a left join master_pegawai b on a.nip=b.nip where tahun = '"+inTahun+"'";
		} else {
			query = "select * from penilaian_kinerja a left join master_pegawai b on a.nip=b.nip";
		}
       
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            Map<String, Object> mapData = new LinkedHashMap<>();
            
            while (rs.next()) {
            	mapData = new LinkedHashMap<>();
            	mapData.put("PENDIDIKAN", rs.getString("pendidikan"));
            	mapData.put("KEHADIRAN", String.valueOf(rs.getInt("kehadiran")));
            	mapData.put("PRILAKU", String.valueOf(rs.getInt("prilaku")));
            	mapData.put("TANGGUNG_JAWAB", String.valueOf(rs.getInt("tanggung_jawab")));
            	mapData.put("INISIATIF", String.valueOf(rs.getInt("inisiatif")));
            	mapData.put("KERJA_SAMA", String.valueOf(rs.getInt("kerja_sama")));
            	mapData.put("DISIPLIN", String.valueOf(rs.getInt("disiplin")));
            	mapData.put("STATUS_KINERJA", rs.getString("status_kinerja"));
            	
				allDataKinerjaPegawai.add(mapData);
            }
        } catch (SQLException | NullPointerException x) {
            x.getMessage();
        }
        return allDataKinerjaPegawai;
    }
}
