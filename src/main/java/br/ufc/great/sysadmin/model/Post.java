package br.ufc.great.sysadmin.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Post extends AbstractModel<Long>{
	private static final long serialVersionUID = 1L;
	private Date date;
	private int likes=0;
	private int amountOfPositive;
	private int amountOfNegative;
	private int amountOfComments;

	@OneToOne
	private Person person;
	@OneToOne
	private Picture picture;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Comment> comments = new LinkedList<>();
	@OneToMany(cascade=CascadeType.ALL)
	private List<Likes> listLikes = new LinkedList<>();
	
	public Post() {
	}
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Picture getPicture() {
		return picture;
	}
	public void setPicture(Picture picture) {
		this.picture = picture;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public void addComment(Comment comment) {
		this.getComments().add(comment);
	}

	public List<Likes> getListLikes() {
		return listLikes;
	}

	public void setListLikes(List<Likes> listLikes) {
		this.listLikes = listLikes;
	}
	
	public void addLike(Likes like) {
		this.getListLikes().add(like);
	}
		
	public int getAmountOfPositive() {
		List<Likes> likes = this.getListLikes();
		int cont=0;
		
		for (Likes element : likes) {
			if (element.isMylike()) {
				cont++;
			}
		}
		amountOfPositive = cont;
		
		return amountOfPositive;
	}

	public void setAmountOfPositive(int amountOfPositive) {
		this.amountOfPositive = amountOfPositive;
	}

	public int getAmountOfNegative() {
		List<Likes> likes = this.getListLikes();
		int cont=0;
		
		for (Likes element : likes) {
			if (!element.isMylike()) {
				cont++;
			}
		}

		amountOfNegative = cont;
		
		return amountOfNegative;
	}

	public void setAmountOfNegative(int amountOfNegative) {
		this.amountOfNegative = amountOfNegative;
	}

	public int getAmountOfComments() {
		amountOfComments = this.getComments().size();
		
		return amountOfComments;
	}

	public void setAmountOfComments(int amountOfComments) {
		this.amountOfComments = amountOfComments;
	}
}