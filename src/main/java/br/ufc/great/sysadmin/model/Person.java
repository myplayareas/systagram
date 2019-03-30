package br.ufc.great.sysadmin.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.ufc.great.sysadmin.util.ManipuladorDatas;

@Entity
public class Person extends AbstractModel<Long>{
	private static final long serialVersionUID = 1L;
	@OneToOne
	private Users user;
	@Column(length=255)
	private String name;
	@Column(length=255)
	private String address;
	@Column(length=255)
	private String city;
	@Column(length=255)
	private String state;
	@Column(length=8)
	private String cep;
	private double latitude=0;
	private double longitude=0;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Comment> comments = new LinkedList<>();
	@OneToMany(cascade=CascadeType.ALL)
	private List<Picture> pictures = new LinkedList<>();
	@OneToMany(cascade=CascadeType.ALL)
	private List<Post> posts = new LinkedList<>();
	@OneToMany(cascade=CascadeType.ALL)
	private List<Likes> likes = new LinkedList<>();

	public Person() {
	}
	
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	
	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void addComment(Comment comment, Person person) {
		comment.setPerson(person);
		this.getComments().add(comment);
	}
	public Comment getMyComment(Long id) {
		List<Comment> list = this.getComments();
		
		for (Comment element : list) {
			if (element.getId()==id) {
				return element;
			}
		}
		return null;
	}

	public void addPicture(Picture picture, Person person) {
		picture.setPerson(person);
		this.getPictures().add(picture);
	}
	public Picture getMyPicture(Long id) {
		List<Picture> list = this.getPictures();
		
		for (Picture element : list) {
			if (element.getId()==id) {
				return element;
			}
		}
		return null;
	}

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public List<Likes> getLikes() {
		return likes;
	}

	public void setLikes(List<Likes> likes) {
		this.likes = likes;
	}
	
	public void addLike(Likes like, Person person) {
		like.setPerson(person);
		this.likes.add(like);
	}
	
	public List<Post> getPostByDateFromTo(Date from , Date to){
		List<Post> myPosts = this.getPosts();
		List<Post> posts = new LinkedList<>();
		
		for (Post element : myPosts) {
			Date date = element.getDate(); 
			if (new ManipuladorDatas().dateIsBetweenDates(date, from, to)) {
				posts.add(element);
			}
		}
		
		return posts;
	}
	
}