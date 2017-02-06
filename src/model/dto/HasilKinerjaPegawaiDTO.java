package model.dto;

import java.util.Date;

public class HasilKinerjaPegawaiDTO {

	private Integer id;
	private String nip;
	private String nama;
	private Integer tahun;
	private String pendidikan;
	private Integer kehadiran;
	private Integer prilaku;
	private Integer tanggung_jawab;
	private Integer inisiatif;
	private Integer kerja_sama;
	private Integer disiplin;
	private Double jumlah_nilai;
	private String status_kinerja;
	private String nip_atasan;
	private Date created_at;
	private Date update_at;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getTahun() {
		return tahun;
	}

	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}

	public String getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(String pendidikan) {
		this.pendidikan = pendidikan;
	}

	public Integer getKehadiran() {
		return kehadiran;
	}

	public void setKehadiran(Integer kehadiran) {
		this.kehadiran = kehadiran;
	}

	public Integer getPrilaku() {
		return prilaku;
	}

	public void setPrilaku(Integer prilaku) {
		this.prilaku = prilaku;
	}

	public Integer getTanggung_jawab() {
		return tanggung_jawab;
	}

	public void setTanggung_jawab(Integer tanggung_jawab) {
		this.tanggung_jawab = tanggung_jawab;
	}

	public Integer getInisiatif() {
		return inisiatif;
	}

	public void setInisiatif(Integer inisiatif) {
		this.inisiatif = inisiatif;
	}

	public Integer getKerja_sama() {
		return kerja_sama;
	}

	public void setKerja_sama(Integer kerja_sama) {
		this.kerja_sama = kerja_sama;
	}

	public Integer getDisiplin() {
		return disiplin;
	}

	public void setDisiplin(Integer disiplin) {
		this.disiplin = disiplin;
	}

	public Double getJumlah_nilai() {
		return jumlah_nilai;
	}

	public void setJumlah_nilai(Double jumlah_nilai) {
		this.jumlah_nilai = jumlah_nilai;
	}

	public String getStatus_kinerja() {
		return status_kinerja;
	}

	public void setStatus_kinerja(String status_kinerja) {
		this.status_kinerja = status_kinerja;
	}

	public String getNip_atasan() {
		return nip_atasan;
	}

	public void setNip_atasan(String nip_atasan) {
		this.nip_atasan = nip_atasan;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdate_at() {
		return update_at;
	}

	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}

}
