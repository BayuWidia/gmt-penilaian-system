package model.dto;

import java.util.Date;

public class KelolaPegawaiDTO {

	private Integer id;
	private String nip;
	private String nip_lama;
	private String nama;
	private String status_karyawan;
	private Integer id_jabatan;
	private Integer status;
	private Date created_at;
	private Date updated_at;

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

	public String getNip_lama() {
		return nip_lama;
	}

	public void setNip_lama(String nip_lama) {
		this.nip_lama = nip_lama;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getStatus_karyawan() {
		return status_karyawan;
	}

	public void setStatus_karyawan(String status_karyawan) {
		this.status_karyawan = status_karyawan;
	}

	public Integer getId_jabatan() {
		return id_jabatan;
	}

	public void setId_jabatan(Integer id_jabatan) {
		this.id_jabatan = id_jabatan;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

}
