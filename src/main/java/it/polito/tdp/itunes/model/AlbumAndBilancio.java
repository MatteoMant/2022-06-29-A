package it.polito.tdp.itunes.model;

public class AlbumAndBilancio implements Comparable<AlbumAndBilancio> {
	
	private Album album;
	private Integer bilancio;
	
	public AlbumAndBilancio(Album album, Integer bilancio) {
		super();
		this.album = album;
		this.bilancio = bilancio;
	}
	
	public Album getAlbum() {
		return album;
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public Integer getBilancio() {
		return bilancio;
	}
	
	public void setBilancio(Integer bilancio) {
		this.bilancio = bilancio;
	}

	@Override
	public int compareTo(AlbumAndBilancio other) {
		return other.getBilancio() - this.getBilancio();
	}
	
	
}
