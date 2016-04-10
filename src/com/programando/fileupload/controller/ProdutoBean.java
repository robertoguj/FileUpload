package com.programando.fileupload.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.programando.fileupload.dao.FotoDAO;
import com.programando.fileupload.dao.ProdutoDAO;
import com.programando.fileupload.model.Foto;
import com.programando.fileupload.model.Produto;

@ManagedBean(name = "mbProduto")
@SessionScoped
public class ProdutoBean implements Serializable {

	private static final long serialVersionUID = 3687029742520233750L;

	private Produto produto = new Produto();
	private ProdutoDAO produtoDAO;
	
	private Produto produtoSelecionado = new Produto();
	private List<Produto> produtos;

	private Foto foto = new Foto();
	private List<Foto> fotos;
	
	private FotoDAO fotoDAO = new FotoDAO();

	public ProdutoBean() {
		produtoDAO = new ProdutoDAO();
		fotoDAO = new FotoDAO();

		produtos = produtoDAO.listAll();
	}

	public void salvaProduto() {

		try {
			produtoDAO.save(produto);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			produto = new Produto();
			produtos = produtoDAO.listAll();

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Produto adicionado", "Produto adicionado"));
		}
	}

	public void salvaFoto() {

		try {
			fotoDAO.save(foto);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			foto = new Foto();

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Foto adicionada", "Foto adicionada"));
		}

	}

	public void processFileUpload(FileUploadEvent uploadEvent) {

		try {
			// Cria um arquivo UploadFile, para receber o arquivo do evento
			UploadedFile arquivo = uploadEvent.getFile();
						
			// Transformar a imagem em bytes para salvar em banco de dados
			byte[] by = uploadEvent.getFile().getContents();
			
			foto.setProduto(produtoSelecionado);
			foto.setImagem(by);
			foto.setNomeArquivo(arquivo.getFileName());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void listaFotosProduto() {

		try {
			ServletContext sContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();

			fotos = fotoDAO.listByProdutos(produtoSelecionado.getCodigo());

			File folder = new File(sContext.getRealPath("/temp"));
			if (!folder.exists())
				folder.mkdirs();

			for (Foto f : fotos) {
				String nomeArquivo = f.getCodigo() + ".jpg";
				String arquivo = sContext.getRealPath("/temp") + File.separator
						+ nomeArquivo;

				criaArquivo(f.getImagem(), arquivo);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void criaArquivo(byte[] bytes, String arquivo) {
		FileOutputStream fos;

		try {
			fos = new FileOutputStream(arquivo);
			fos.write(bytes);

			fos.flush();
			fos.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public List<Foto> getFotos() {
		return fotos;
	}
}